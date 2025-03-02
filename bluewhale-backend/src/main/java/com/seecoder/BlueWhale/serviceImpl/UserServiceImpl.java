package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.annotation.RequiresRoleCustomer;
import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.Store;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.po.PhoneAddressCandidate;
import com.seecoder.BlueWhale.repository.PhoneAddressCandidatesRepository;
import com.seecoder.BlueWhale.repository.StoreRepository;
import com.seecoder.BlueWhale.repository.UserRepository;
import com.seecoder.BlueWhale.service.UserService;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.PhoneAddressCandidateVO;
import com.seecoder.BlueWhale.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: GaoZhaoInteger
 * @Date: 14:46 2023/11/26
 * <p>
 * 注册登录功能实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PhoneAddressCandidatesRepository phoneAddressCandidatesRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    StoreRepository storeRepository;

    @Override
    public Boolean register(UserVO userVO) {
        User user = userRepository.findByPhone(userVO.getPhone());
        if (user != null) {
            throw BlueWhaleException.phoneAlreadyExists();
        }
        User newUser = userVO.toPO();
        newUser.setCreateTime(new Date());
        userRepository.save(newUser);
        return true;
    }

    @Override
    public String login(String phone, String password) {
        User user = userRepository.findByPhone(phone);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            // 该电话对应用户不存在 或 密码不匹配
            throw BlueWhaleException.phoneOrPasswordError();
        }
        return tokenUtil.getToken(user);
    }

    @Override
    public UserVO getInformation() {
        User user = securityUtil.getCurrentUser();
        if (user.getRole() == RoleEnum.STAFF) {
            return wrapWithStoreName(user.toVO());
        }
        user.setPassword(null);
        return user.toVO();
    }

    @Override
    public Boolean updateInformation(UserVO userVO) {
        User user = securityUtil.getCurrentUser();
        if (userVO.getPassword() != null) {
            BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(userVO.getPassword()));
        }
        if (userVO.getName() != null) {
            user.setName(userVO.getName());
        }
        userRepository.save(user);
        return true;
    }

    /**
     * 用户获取备选电话地址
     * <p>
     * &#064;Return  List<PhoneAddressCandidateVO>型，当前用户的备选电话地址列表
     */
    @Override
    @RequiresRoleCustomer
    public List<PhoneAddressCandidateVO> getPhoneAddressCandidates() {
        return phoneAddressCandidatesRepository.findAllByUserId(securityUtil.getCurrentUser().getId()).stream().map(PhoneAddressCandidate::toVO).collect(Collectors.toList());
    }

    /**
     * 用户更新备选电话地址
     * <p>
     * &#064;Param  List<PhoneAddressCandidateVO>型，前端更新后的备选电话地址列表
     * &#064;Return  成功返回True
     */
    @Override
    @RequiresRoleCustomer
    public Boolean updatePhoneAddressCandidates(List<PhoneAddressCandidateVO> phoneAddressCandidateVOList) {
        Integer id = securityUtil.getCurrentUser().getId();
        phoneAddressCandidateVOList.forEach(phoneAddressCandidateVO -> phoneAddressCandidateVO.setUserId(id));
        if (phoneAddressCandidatesRepository.existsByUserId(id)) {
            phoneAddressCandidatesRepository.deleteByUserId(id);
        }
        phoneAddressCandidatesRepository.saveAll(phoneAddressCandidateVOList.stream().map(PhoneAddressCandidateVO::toPO).collect(Collectors.toList()));
        return true;
    }

    public UserVO wrapWithStoreName(UserVO userVO) {
        Integer storeId = userVO.getStoreId();
        if (storeId == null) {
            return userVO;
        }
        Store store = storeRepository.findById(storeId).get();
        userVO.setStoreName(store.getName());
        return userVO;
    }
}
