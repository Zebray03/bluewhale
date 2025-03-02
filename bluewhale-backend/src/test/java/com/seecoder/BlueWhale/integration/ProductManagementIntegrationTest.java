package com.seecoder.BlueWhale.integration;

import com.seecoder.BlueWhale.po.Product;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.repository.ProductRepository;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
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
class ProductManagementIntegrationTest {

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    private Boolean createProduct() {
        String requestBody = "{\"name\":\"Shirt\"" +
                ",\"storeId\":" + securityUtil.getCurrentUser().getStoreId() +
                ",\"photo_url_list\":\"This is a photo.\"" +
                ",\"price\":100" +
                ",\"category\":\"CLOTHES\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, forHeaders());
        ResponseEntity<ResultVO<Boolean>> response = restTemplate.exchange(
                "/api/products",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResultVO<Boolean>>() {
                }
        );
        assert (response.getStatusCode() == HttpStatus.OK);
        ResultVO<Boolean> result = response.getBody();
        assert result.getCode().equals("000");
        assert result.getResult();
        return true;
    }

    private HttpHeaders forHeaders() {
        User user = new User();
        user.setId(5);
        user.setPassword("123456");
        HttpHeaders headers = new HttpHeaders();
        String token=tokenUtil.getToken(user);
        headers.add("token", token);
        System.out.println(tokenUtil.verifyToken(token));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // 以店员登录开始，完成创建商品并补货的流程
    @Test
    void allTest() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(forHeaders());
        assert createProduct();
        Product product = productRepository.findByStoreIdAndName(securityUtil.getCurrentUser().getStoreId(), "shirt");
        Integer productId = product.getId();
        Integer number = 50;
        String url = String.format("/api/products/{%d}/stock/?number=%d", productId, number);
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
        assert resultVO.getResult();
        assert product.getStock() == 50;
    }
}
