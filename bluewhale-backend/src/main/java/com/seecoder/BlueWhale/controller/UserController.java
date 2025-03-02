package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.service.UserService;
import com.seecoder.BlueWhale.serviceImpl.StoreServiceImpl;
import com.seecoder.BlueWhale.vo.PhoneAddressCandidateVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import com.seecoder.BlueWhale.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    //参数校验
    @PostMapping("/register")
    public ResultVO<Boolean> register(@RequestBody @Validated UserVO userVO) {
        return ResultVO.buildSuccess(userService.register(userVO));
    }

    @PostMapping("/login")
    public ResultVO<String> login(@RequestParam("phone") String phone, @RequestParam("password") String password) {
       log.info(phone);
        return ResultVO.buildSuccess(userService.login(phone, password));
    }

    @GetMapping("/information")
    public ResultVO<UserVO> getInformation() {
        return ResultVO.buildSuccess(userService.getInformation());
    }

    @PostMapping("/information")
    public ResultVO<Boolean> updateInformation(@RequestBody UserVO userVO) {
        return ResultVO.buildSuccess(userService.updateInformation(userVO));
    }

    @GetMapping("/candidates")
    public ResultVO<List<PhoneAddressCandidateVO>> getPhoneAddressCandidates() {
        return ResultVO.buildSuccess(userService.getPhoneAddressCandidates());
    }

    @PostMapping("/candidates")
    public ResultVO<Boolean> updatePhoneAddressCandidates(@RequestBody List<PhoneAddressCandidateVO> phoneAddressCandidateVOList) {
        return ResultVO.buildSuccess(userService.updatePhoneAddressCandidates(phoneAddressCandidateVOList));
    }
}
