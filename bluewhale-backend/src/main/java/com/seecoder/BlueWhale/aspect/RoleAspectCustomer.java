package com.seecoder.BlueWhale.aspect;


import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.util.SecurityUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * &#064;Author:  B Zhang
 * &#064;Date:  2024/6/12
 * 自定义注解@RequiresRoleCustomer的切面类
 */
@Aspect
@Component
public class RoleAspectCustomer {
    @Autowired
    private SecurityUtil securityUtil;

    @Before("@annotation(com.seecoder.BlueWhale.annotation.RequiresRoleCustomer)")
    public void checkWhetherCustomerRole() {
        User user = securityUtil.getCurrentUser();
        if (user.getRole() != RoleEnum.CUSTOMER) {
            throw BlueWhaleException.NotCustomerError();
        }
    }
}
