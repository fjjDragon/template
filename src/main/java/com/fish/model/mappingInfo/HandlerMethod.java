package com.fish.model.mappingInfo;

import java.lang.reflect.Method;

public class HandlerMethod {

    private final Class<?> beanType;
    private final Method method;

    public HandlerMethod(Class<?> beanType, Method method) {
        this.beanType = beanType;
        this.method = method;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Method getMethod() {
        return method;
    }

}
