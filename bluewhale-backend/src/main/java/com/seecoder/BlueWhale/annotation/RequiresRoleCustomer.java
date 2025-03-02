package com.seecoder.BlueWhale.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * &#064;Author:  B Zhang
 * &#064;Date:  2024/6/12
 * 自定义注解@RequiresRoleCustomer类
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRoleCustomer {}