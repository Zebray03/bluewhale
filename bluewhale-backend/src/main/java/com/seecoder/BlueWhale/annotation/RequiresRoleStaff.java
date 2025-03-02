package com.seecoder.BlueWhale.annotation;

import com.seecoder.BlueWhale.po.Product;
import com.seecoder.BlueWhale.vo.ProductVO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * &#064;Author:  K Zhang
 * &#064;Date:  2024/6/13
 * 自定义注解@RequireRoleStaff类
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRoleStaff {
}
