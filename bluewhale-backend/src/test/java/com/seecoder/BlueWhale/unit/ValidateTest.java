package com.seecoder.BlueWhale.unit;

import com.seecoder.BlueWhale.controller.OrderController;
import com.seecoder.BlueWhale.controller.StoreController;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.serviceImpl.OrderServiceImpl;
import com.seecoder.BlueWhale.serviceImpl.StoreServiceImpl;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.OrderVO;
import com.seecoder.BlueWhale.vo.StoreVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ValidateTest {
    private MockMvc mockMvc;
    @Mock
    private OrderServiceImpl orderServiceImpl;

    @Autowired
    private OrderController orderController;
    private String token;
    private MockHttpSession session;
    private HttpHeaders headers;
    @Autowired
    TokenUtil tokenUtil;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc= MockMvcBuilders.standaloneSetup(orderController).build();
    }
    HttpHeaders forHeaders() {
        User user = new User();
        user.setId(11);
        user.setPassword("123456");
        HttpHeaders headers = new HttpHeaders();
        token=tokenUtil.getToken(user);
        headers.add("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
    MockHttpSession getSession() {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("currentUser", tokenUtil.getUser(token)); // 替换 tokenUtil.getUser(token) 为实际获取用户的方法
        return session;
    }

    @Test
    public void test() throws Exception {
        List<StoreVO> resultVO=new LinkedList<>();
        resultVO.add(new StoreVO());
        headers=forHeaders();
        session=getSession();
        // (2)构建请求
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/orders")
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers)
                .session(session);

        // (3)发送请求，获取请求结果
        ResultActions perform = mockMvc.perform(request);

        // (4)请求结果校验
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        // (5)校验返回信息
        System.out.println(response.getContentAsString());
    }

}
