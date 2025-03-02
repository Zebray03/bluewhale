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
 * 自定义注解@RequiresRoleManager的切面类
 */
@Aspect
@Component
public class RoleAspectManager {
    @Autowired
    private SecurityUtil securityUtil;

    @Before("@annotation(com.seecoder.BlueWhale.annotation.RequiresRoleManager)")
    public void checkWhetherManagerRole() throws Exception {
        User user = securityUtil.getCurrentUser();
        if (user.getRole() != RoleEnum.MANAGER) {
            throw BlueWhaleException.NotManagerError();
        }
    }
}
