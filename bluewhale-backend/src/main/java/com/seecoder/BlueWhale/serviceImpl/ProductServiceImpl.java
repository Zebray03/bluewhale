package com.seecoder.BlueWhale.serviceImpl;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.seecoder.BlueWhale.enums.CategoryEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.Order;
import com.seecoder.BlueWhale.po.Product;
import com.seecoder.BlueWhale.po.Store;
import com.seecoder.BlueWhale.repository.OrderRepository;
import com.seecoder.BlueWhale.repository.ProductRepository;
import com.seecoder.BlueWhale.repository.StoreRepository;
import com.seecoder.BlueWhale.repository.UserRepository;
import com.seecoder.BlueWhale.service.ProductService;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.ProductSearchVO;
import com.seecoder.BlueWhale.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    SecurityUtil securityUtil;

    @Override
    public Boolean create(ProductVO productVO) {
        if (productRepository.findByStoreIdAndName(productVO.getStoreId(), productVO.getName()) != null) {
            throw BlueWhaleException.nameAlreadyExists();
        }
        if(!Objects.equals(productVO.getStoreId(), securityUtil.getCurrentUser().getStoreId())){
            throw BlueWhaleException.NotStaffForSameStore();
        }
        Product product = productVO.toPO();
        product.setRating(0.0);
        product.setNumber(0);
        product.setSalesAmount(0);
        product.setStock(0);
        try {
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                throw BlueWhaleException.storeNotExists();
            }
            return false;
        }
    }
    @Override
    public Boolean addStock(Integer id, Integer number) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw BlueWhaleException.productNotExists();
        }
        product.setStock(product.getStock() + number);
        productRepository.save(product);
        return true;
    }

    @Override
    public List<ProductVO> getAllProducts(Integer storeId) {
        Store store = storeRepository.findById(storeId).orElse(null);
        if (store == null) {
            throw BlueWhaleException.storeNotExists();
        }
        return productRepository.findAllByStoreId(storeId).stream().map(Product::toVO).collect(Collectors.toList());
    }

    @Override
    public ProductVO getProduct(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw BlueWhaleException.productNotExists();
        }
        return product.toVO();
    }

    @Override
    public ProductSearchVO getProductsWithCondition(ProductSearchVO searchNode) {
        // 构造用于模糊查询的名称字符串
        String likeName = String.format("%%%s%%", searchNode.getName());

        // 构建用于构造查询条件的 andPredicate 对象
        PredicateBuilder<Product> andPredicate = Specifications.<Product>and()
                .like(searchNode.getName() != null, "name", likeName) // 如果搜索名称不为空，则添加名称模糊匹配条件
                .ge(searchNode.getLowValue() != null, "price", searchNode.getLowValue()) // 如果最低价格不为空，则添加价格大于等于条件
                .le(searchNode.getHighValue() != null, "price", searchNode.getHighValue()); // 如果最高价格不为空，则添加价格小于等于条件

        PredicateBuilder<Product> orPredicate = null; // 初始化 orPredicate 为 null

        // 如果搜索节点中包含类别条件
        if (searchNode.getCategory() != null) {
            CategoryEnum cat = searchNode.getCategory();

            // 根据类别枚举不同进行不同的处理
            switch (cat) {
                case FOOD:
                    // 如果是 FOOD 类别，则构建 orPredicate 条件，匹配 DRINK、SNACK、HEALTHY、ORGANIC 任一类别
                    orPredicate = Specifications.<Product>or()
                            .eq("category", CategoryEnum.DRINK)
                            .eq("category", CategoryEnum.SNACK)
                            .eq("category", CategoryEnum.HEALTHY)
                            .eq("category", CategoryEnum.ORGANIC);
                    break;
                default:
                    // 其他类别情况下，直接添加等于条件到 andPredicate 中
                    andPredicate.eq("category", cat);
                    break;
            }
        }

        // 构建最终的 andSpecification 条件
        Specification<Product> andSpecification = andPredicate.build();
        Specification<Product> orSpecification;
        Specification<Product> resultSpecification;

        // 如果存在 orPredicate 条件，则构建 orSpecification，并合并到 resultSpecification 中
        if (orPredicate != null) {
            orSpecification = orPredicate.build();
            resultSpecification = andSpecification.and(orSpecification);
        } else {
            // 否则直接使用 andSpecification 作为最终的 resultSpecification
            resultSpecification = andSpecification;
        }

        // 新建一个 ProductSearchVO 对象作为返回结果
        ProductSearchVO result = new ProductSearchVO();

        // 查询符合条件的产品，并转换为 ProductVO 对象列表
        List<ProductVO> productVOS = productRepository.findAll(resultSpecification)
                .stream()
                .map(Product::toVO)
                .collect(Collectors.toList());

        List<ProductVO> reVOS = new ArrayList<>(); // 新建一个用于分页后的结果集合

        int page_size = 10; // 默认页大小为 10
        int page_index = 0; // 默认页索引为 0

        // 如果搜索节点中包含指定的页大小，则使用搜索节点中的页大小
        if (searchNode.getPage_size() != null) {
            page_size = searchNode.getPage_size();
        }

        // 如果搜索节点中包含指定的页索引，则计算实际的页索引
        if (searchNode.getPage_index() != null) {
            page_index = Math.max(searchNode.getPage_index() - 1, 0);
        }

        // 根据分页条件从 productVOS 中取出对应页的数据放入 reVOS 中
        for (int i = page_index * page_size, j = 0; i < productVOS.size() && j < page_size; i++, j++) {
            reVOS.add(productVOS.get(i));
        }

        // 设置返回结果的页索引、页大小、分页后的产品列表和总页数
        result.setPage_index(page_index + 1);
        result.setPage_size(page_size);
        result.setREproductList(reVOS);
        result.setPage_num((productVOS.size() - 1) / page_size + 1);

        return result; // 返回最终的查询结果对象
    }


    @Override
    public List<CommentVO> getComments(Integer productId) {
        List<Order> orders = orderRepository.findByProductIdAndFinishTimeNotNull(productId);
        return orders.stream().map(x -> {
            CommentVO commentVO = new CommentVO();
            commentVO.setOrderId(x.getId());
            commentVO.setRating(x.getRating());
            commentVO.setTime(x.getFinishTime());
            commentVO.setComment(x.getContent());
            commentVO.setUserId(x.getUserId());
            commentVO.setUserName(userRepository.findById(x.getUserId()).get().getName());
            return commentVO;
        }).collect(Collectors.toList());
    }
}
