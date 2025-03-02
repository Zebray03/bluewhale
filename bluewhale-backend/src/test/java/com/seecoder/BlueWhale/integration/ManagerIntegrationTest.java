package com.seecoder.BlueWhale.integration;

import com.seecoder.BlueWhale.po.Product;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.ResultVO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ManagerIntegrationTest {

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    private TestRestTemplate restTemplate;

    public Boolean createStore() {
        String requestBody = "{\"name\":\"KFC\"" +
                ",\"location\":\"Room 123, Floor 1\"" +
                ",\"logo_url\":\"This is a photo.\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, forHeaders());
        ResponseEntity<ResultVO<Boolean>> response = restTemplate.exchange(
                "/api/stores",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResultVO<Boolean>>() {
                }
        );
        System.out.println(response.getStatusCode());
        assert (response.getStatusCode() == HttpStatus.OK);
        ResultVO<Boolean> result = response.getBody();
        assert result.getCode().equals("000");
        assert result.getResult();
        return true;
    }

    public HttpHeaders forHeaders() {
        User user = new User();
        user.setId(1);
        user.setPassword("123456");
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", tokenUtil.getToken(user));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // 以经理身份登录，创建商店并获取当前报表
    @Test
    void allTest() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(forHeaders());
        assert createStore();
        String url = "/api/excel";
        ResponseEntity<ResultVO<String>> res = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResultVO<String>>() {
                }
        );
        assert (res.getStatusCode() == HttpStatus.OK);
        ResultVO<String> resultVO = res.getBody();
        assert (resultVO.getCode().equals("000"));
        System.out.println(resultVO);
    }
}
