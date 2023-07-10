package com.vip.file.aspect;

import java.lang.annotation.*;

/**
 * @author zkc
 * 自定义注解
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogTrack {
    String value() ;
}
