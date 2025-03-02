package com.seecoder.BlueWhale.integration;

import java.util.List;

import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.serviceImpl.strategy.CalculateStrategy;
import com.seecoder.BlueWhale.serviceImpl.strategy.Context;
import com.seecoder.BlueWhale.serviceImpl.strategy.FillReductionCouponCalculateStrategy;
import com.seecoder.BlueWhale.serviceImpl.strategy.SpecialCouponCalculateStrategy;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.CouponVO;
import com.seecoder.BlueWhale.vo.OrderVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.client.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.*;;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PayIntegrationTests {

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    OrderVO createOrder() {
        String requestBody = "{\"productId\":\"7\",\"amount\":\"10\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<ResultVO<OrderVO>> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResultVO<OrderVO>>() {
                }
        );
        assert (response.getStatusCode() == HttpStatus.OK);
        ResultVO<OrderVO> result = response.getBody();
        assert (result.getCode().equals("000"));
        return result.getResult();
    }

    HttpHeaders forHeaders() {
        User user = new User();
        user.setId(11);
        user.setPassword("123456");
        HttpHeaders headers = new HttpHeaders();

        headers.add("token", tokenUtil.getToken(user));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // 以登录开始，完成订单下单，选用优惠券到支付的整个过程
    @Test
    void allTest() {
        headers=forHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        //领取优惠券
        ResponseEntity<ResultVO<Boolean>> response = restTemplate.exchange(
                "/api/coupons/receive?couponGroupId=7",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResultVO<Boolean>>() {
                }
        );
        assert (response.getStatusCode() == HttpStatus.OK);
        ResultVO<Boolean> result = response.getBody();
        assert (result.getCode().equals("000"));

        // 创建订单
        OrderVO orderVO = createOrder();
        int orderId = orderVO.getId();
        int couponId = 7;

        // 支付订单
        String url = String.format("/api/orders/pay/?orderId=%d&couponId=%d", orderId, couponId);
        ResponseEntity<ResultVO<Boolean>> res = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResultVO<Boolean>>() {
                }
        );
        assert (res.getStatusCode() == HttpStatus.OK);
        ResultVO<Boolean> resultVO = res.getBody();
        assert (resultVO.getCode().equals("000"));
    }
}
