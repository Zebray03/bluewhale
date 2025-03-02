package com.seecoder.BlueWhale.unit;

import com.seecoder.BlueWhale.controller.StoreController;
import com.seecoder.BlueWhale.repository.StoreRepository;
import com.seecoder.BlueWhale.serviceImpl.StoreServiceImpl;
import com.seecoder.BlueWhale.serviceImpl.UserServiceImpl;
import com.seecoder.BlueWhale.vo.StoreVO;
import com.seecoder.BlueWhale.vo.UserVO;
import com.seecoder.BlueWhale.po.Store;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createStore() throws Exception {
        UserVO userVO = new UserVO();
        userVO.setStoreId(1);
        Store store = new Store();
        if(userVO.getStoreId()==1){
            store.setName("have");
        }else {
            store.setName("done");
        }
        when(storeRepository.findById(userVO.getStoreId())).thenReturn(Optional.of(store));
        System.out.println(userService.wrapWithStoreName(userVO).getStoreName());
    }


}
