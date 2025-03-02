package com.seecoder.BlueWhale.unit;

import com.seecoder.BlueWhale.controller.StoreController;
import com.seecoder.BlueWhale.serviceImpl.StoreServiceImpl;
import com.seecoder.BlueWhale.vo.StoreVO;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.LinkedList;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CotrollerUnitTest {
    private MockMvc mockMvc;
    @Mock
    private StoreServiceImpl storeServiceimpl;

    @InjectMocks
    private StoreController storeController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc= MockMvcBuilders.standaloneSetup(storeController).build();
    }
    @Test
    public void test() throws Exception {
        List<StoreVO>resultVO=new LinkedList<>();
        resultVO.add(new StoreVO());
        // (2)构建请求
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/stores/all")
                .contentType("text/html")
                .accept(MediaType.APPLICATION_JSON);

        // (3)发送请求，获取请求结果
        ResultActions perform = mockMvc.perform(request);

        // (4)请求结果校验
        perform.andExpect(MockMvcResultMatchers.status().isOk());
        when (storeServiceimpl.getAllStores()).thenReturn(resultVO);
        MvcResult mvcResult = perform.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        // (5)校验返回信息
        System.out.println(response.getContentAsString());
    }

}
