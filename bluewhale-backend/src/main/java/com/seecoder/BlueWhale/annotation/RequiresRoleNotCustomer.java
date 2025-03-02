package com.seecoder.BlueWhale.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * &#064;Author:  B Zhang
 * &#064;Date:  2024/6/12
 * 自定义注解@RequiresRoleNotCustomer类
 */
@Target(ElementType.METHOD) // 表示该注解用于标记方法
@Retention(RetentionPolicy.RUNTIME) // 表示该注解会在运行时保留，可以通过反射读取
public @interface RequiresRoleNotCustomer {

}