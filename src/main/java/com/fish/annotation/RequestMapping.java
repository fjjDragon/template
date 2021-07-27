package com.fish.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * uri
     *
     * @return
     */
    String value() default "";

    /**
     * http请求方法类型
     *
     * @return
     */
    String method() default "GET";

    /**
     * 权限审核
     *
     * @return
     */
    String permission() default "";
}
