package com.seecoder.BlueWhale.util.recommend.service;

import com.seecoder.BlueWhale.enums.OrderStatusEnum;
import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.po.Order;
import com.seecoder.BlueWhale.po.Product;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.repository.OrderRepository;
import com.seecoder.BlueWhale.repository.ProductRepository;
import com.seecoder.BlueWhale.repository.UserRepository;
import com.seecoder.BlueWhale.util.recommend.dto.RelateDTO;
import com.seecoder.BlueWhale.vo.ProductVO;
import com.seecoder.BlueWhale.vo.UserVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tarzan
 */
@Data
@Slf4j
@Component
public class FileDataSource {

@Autowired
private OrderRepository orderRepository;
@Autowired
private UserRepository userRepository;
@Autowired
private ProductRepository productRepository;
/**
     * 方法描述: 读取基础数据
     *
     * @Return {@link List<RelateDTO>}
     * @author tarzan
     * @date 2020年07月31日 16:53:40
     */
    public  List<RelateDTO> getData() {
        List<RelateDTO> relateList = new ArrayList<>();
        List<Order> orders = orderRepository.findAll().stream().
                filter((a)->(a.getStatus().equals(OrderStatusEnum.DONE))).collect(Collectors.toList());
        for (Order order : orders) {
            RelateDTO relateDTO = new RelateDTO(order.getUserId(),order.getProductId(),
                    (order.getRating() == null ? 2.5 : order.getRating())*1.0);
            relateList.add(relateDTO);
        }
        return relateList;
    }

    public  List<UserVO> getUserData() {
        return userRepository.findAll().stream()
                .filter((a)->(a.getRole()== RoleEnum.CUSTOMER))
                .map(User::toVO).collect(Collectors.toList());
    }


    public  List<ProductVO> getItemData() {
    return productRepository.findAll().stream()
            .map(Product::toVO)
            .collect(Collectors.toList());

    }


}

