package com.seecoder.BlueWhale.unit;

import com.seecoder.BlueWhale.controller.StoreController;
import com.seecoder.BlueWhale.repository.StoreRepository;
import com.seecoder.BlueWhale.serviceImpl.StoreServiceImpl;
import com.seecoder.BlueWhale.vo.StoreVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoreServiceTest {
    private MockMvc mockMvc;
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private StoreServiceImpl storeServiceImpl;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc= MockMvcBuilders.standaloneSetup(storeServiceImpl).build();
    }

    @Test
    public void createStore() throws Exception {
        StoreVO storeVO = new StoreVO();
        storeVO.setName("qwe");

        when(storeRepository.findByName(storeVO.getName())).thenReturn(storeVO.toPO());
        when(storeRepository.save(storeVO.toPO())).thenReturn(storeVO.toPO());
        storeServiceImpl.create(storeVO);
    }
}
