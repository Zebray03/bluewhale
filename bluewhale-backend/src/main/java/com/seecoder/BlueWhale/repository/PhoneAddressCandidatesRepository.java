package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.po.PhoneAddressCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PhoneAddressCandidatesRepository  extends JpaRepository<PhoneAddressCandidate, Integer> {
    Boolean existsByUserId(Integer userId);
    @Transactional
        // 为执行该方法创建一个事务
        // 遭遇异常将回滚
    void deleteByUserId(Integer userId);
    List<PhoneAddressCandidate> findAllByUserId(Integer userId);
}
