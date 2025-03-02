package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.vo.PhoneAddressCandidateVO;
import com.seecoder.BlueWhale.vo.UserVO;

import java.util.List;

public interface UserService {
    Boolean register(UserVO userVO);

    String login(String phone,String password);

    UserVO getInformation();

    Boolean updateInformation(UserVO userVO);

    List<PhoneAddressCandidateVO> getPhoneAddressCandidates();

    Boolean updatePhoneAddressCandidates(List<PhoneAddressCandidateVO> phoneAddressCandidateVOList);
}
