package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.po.Product;
import com.seecoder.BlueWhale.repository.ProductRepository;
import com.seecoder.BlueWhale.service.RecommendService;
import com.seecoder.BlueWhale.util.recommend.core.ItemCF;
import com.seecoder.BlueWhale.util.recommend.core.UserCF;
import com.seecoder.BlueWhale.util.recommend.dto.RelateDTO;
import com.seecoder.BlueWhale.util.recommend.service.FileDataSource;
import com.seecoder.BlueWhale.vo.ProductVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired

    ProductRepository productRepository;
    @Autowired
    FileDataSource fileDataSource;
    @Autowired
    UserCF userCF;
    @Autowired
    ItemCF itemCF;
    int Top=2;
    private static final Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Override

    public List<ProductVO>userCfRecommend(int userId){
    List<RelateDTO> data=fileDataSource.getData();
    List<Integer>recommendations=userCF.recommend(userId,data);
    //新用户的冷启动

    if(recommendations==null){
      return  productRepository.findAll().stream().
              sorted(((a,b)->Double.compare(b.getRating(),a.getRating()))).limit(Top).
              map(Product::toVO).
              collect(Collectors.toList());
    }
    return productRepository.findAll().stream()
            .filter(a->(recommendations.contains(a.getId()))).limit(Top)
            .map(Product::toVO).collect(Collectors.toList());
    }

    @Override
    public List<ProductVO>productCfRecommend(int productId){
        //解决商品的冷启动，推荐相似且评分较高的商品
        List<RelateDTO> data=fileDataSource.getData();
        List<Integer>recommendations=itemCF.recommend(productId,data);
        if(recommendations==null){
            //基于相同品类
            Product product=productRepository.findById(productId).orElse(null);
            log.info(compare(product).toString());
            List<Product> products=productRepository.findAll().stream()
                    .filter(a->!(a.getId().equals(product.getId())))
                    .filter((a)->(compare(a).equals(compare(product))))
                    .sorted((a,b)->Double.compare(b.getRating(),a.getRating())).limit(Top)
                    .collect(Collectors.toList());
            if(products.isEmpty()){
                //如果没有同类商品2，返回评分高的
                return  productRepository.findAll().stream().
                        sorted(((a,b)->Double.compare(b.getRating(),a.getRating()))).limit(Top).
                        map(Product::toVO).
                        collect(Collectors.toList());
            }else {
                return products.stream().map(Product::toVO).collect(Collectors.toList());
            }
        }
        return productRepository.findAll().stream()
                .filter(a->(recommendations.contains(a.getId()))).limit(Top)
                .map(Product::toVO).collect(Collectors.toList());
    }

    public Integer compare(Product a){
        switch (a.getCategory()){
            case FOOD:
            case SNACK:
            case DRINK:
            case HEALTHY:
            case ORGANIC:
                return 1;
            case CLOTHES:
                return 2;
            case FURNITURE:
                return 3;
            case ELECTRONICS:
                return 4;
            case ENTERTAINMENT:
                return 5;
            case SPORTS:
                return 6;
            case LUXURY:
                return 7;
        }
        return 0;
    }
}