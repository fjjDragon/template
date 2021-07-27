package com.fish.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface ClassField {
    String value() default "";

    /**
     * 标记是否需要序列化
     *
     * @return
     */
    boolean serialize() default true;
}
