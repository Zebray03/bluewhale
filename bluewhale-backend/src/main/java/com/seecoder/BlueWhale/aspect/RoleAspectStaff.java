package com.seecoder.BlueWhale.aspect;

import com.seecoder.BlueWhale.annotation.RequiresRoleStaff;
import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.vo.ProductVO;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * &#064;Author:  K Zhang
 * &#064;Date:  2024/6/13
 * 自定义注解@RequireRoleStaff的切面类
 */
@Aspect // 声明这是一个切面类
@Component // 将该切面类作为Spring的组件进行管理
public class RoleAspectStaff {
    @Autowired
    private SecurityUtil securityUtil;


    @Before("@annotation(com.seecoder.BlueWhale.annotation.RequiresRoleStaff)")
    public void checkWhetherStaffRole() {
        User user = securityUtil.getCurrentUser();
        if (user.getRole() != RoleEnum.STAFF) {
            throw BlueWhaleException.NotStaffError();
        }
    }
}
