package com.seecoder.BlueWhale;

import com.mchange.util.AssertException;
import com.seecoder.BlueWhale.controller.CouponController;
import com.seecoder.BlueWhale.controller.StoreController;
import com.seecoder.BlueWhale.enums.CouponTypeEnum;
import com.seecoder.BlueWhale.enums.OrderStatusEnum;
import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.service.CouponService;
import com.seecoder.BlueWhale.service.StoreService;
import com.seecoder.BlueWhale.service.UserService;
import com.seecoder.BlueWhale.serviceImpl.CouponServiceImpl;
import com.seecoder.BlueWhale.serviceImpl.OrderServiceImpl;
import com.seecoder.BlueWhale.serviceImpl.StoreServiceImpl;
import com.seecoder.BlueWhale.serviceImpl.UserServiceImpl;
import com.seecoder.BlueWhale.serviceImpl.strategy.SpecialCouponCalculateStrategy;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.CouponGroupVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import com.seecoder.BlueWhale.vo.StoreVO;
import com.seecoder.BlueWhale.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BlueWhaleApplicationTests {
	@Mock
	UserServiceImpl userServiceImpl;
	@Mock
	StoreServiceImpl storeServiceImpl;
	@Mock
	StoreRepository storeRepository;
	@Mock
	UserRepository userRepository;


	@InjectMocks
	OrderServiceImpl orderServiceImpl;
	private MockMvc mockMvc;

	@Mock
	TokenUtil tokenUtil;

	@Mock
	OrderRepository orderRepository;
	@Mock
	SecurityUtil securityUtil;
	@Mock CouponRepository couponRepository;
	@Mock CouponGroupRepository couponGroupRepository;
	@Mock ProductRepository productRepository;
	@Autowired
	StoreController storeController;
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc= MockMvcBuilders.standaloneSetup(storeController).build();
	}

	public void payOrder() throws Exception {
		User user = new User();
		Order order = new Order();
		Coupon coupon = new Coupon();
		Product product = new Product();
		CouponGroup couponGroup = new CouponGroup();
		order.setStatus(OrderStatusEnum.UNPAID);
		Integer orderid=1;
		Integer couponId=0;
		when(securityUtil.getCurrentUser()).thenReturn(user);
		when(orderRepository.findById(orderid)).thenReturn(Optional.of(order));
		when(productRepository.findById(order.getProductId())).thenReturn(Optional.of(product));
		when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
		when(couponGroupRepository.findById(coupon.getGroupId())).thenReturn(Optional.of(couponGroup));
		orderServiceImpl.pay(orderid,0);
	}
	void contextLoads() {
		User user=new User();
		user.setId(1);
		user.setPassword("123456");
		System.out.println(tokenUtil.getToken(user));
	}

	@Test
	public void test() throws Exception {
		ResultVO<List<StoreVO>>resultVO=new ResultVO<>();
		// (2)构建请求
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/stores/all")
				.contentType("text/html")
				.accept(MediaType.APPLICATION_JSON);

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
