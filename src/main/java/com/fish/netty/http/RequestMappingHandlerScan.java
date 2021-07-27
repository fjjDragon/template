package com.fish.netty.http;

import com.fish.annotation.Controller;
import com.fish.annotation.RequestMapping;
import com.fish.model.mappingInfo.HandlerMethod;
import com.fish.model.mappingInfo.RequestMappingInfo;
import com.fish.util.ClassUtil;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author: fjjdragon
 * @date: 2021-07-24 14:05
 */
@Slf4j
public class RequestMappingHandlerScan {


    private static RequestMappingHandlerScan instance = new RequestMappingHandlerScan();

    public static RequestMappingHandlerScan getInstance() {
        return instance;
    }

    private RequestMappingHandlerScan() {
    }

    private Set<Class<?>> annotationClasses;
    private Map<String, RequestMappingInfo> pathMap = new HashMap<>();
    private Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = new HashMap<>();

    public void init(Package pack) {
        try {
            List<Class<?>> list = ClassUtil.getAllClassByPackageName(pack);
            annotationClasses = getAnnotationClasses(list);

        } catch (Exception e) {
            log.error("scan package error :" + pack, e);
        }
    }

    private Set<Class<?>> getAnnotationClasses(List<Class<?>> allClass) {
        Set<Class<?>> annotationClasses = new HashSet<>(24);
        try {
            for (Class<?> clazz : allClass) {
                if (clazz.isAnnotationPresent(Controller.class)) {
                    annotationClasses.add(clazz);
                    getAnotationMethod(clazz);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return annotationClasses;
    }

    /**
     * 获取被@RequestMapping和注解的方法
     *
     * @param clazz
     * @return
     */
    public void getAnotationMethod(Class<?> clazz) throws IllegalAccessException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 取得访问权限
//            String mod = Modifier.toString(method.getModifiers());
            if (!method.trySetAccessible()) {
                log.warn("The method can't access :" + method);
            }
            RequestMapping an = method.getAnnotation(RequestMapping.class);
            if (an != null) {
                HandlerMethod handlerMethod = new HandlerMethod(clazz, method);
                RequestMapping ann = clazz.getAnnotation(RequestMapping.class);
                if (ann != null) {
                    //url路径
                    String path = ann.value() + an.value();

                    String permission = an.permission();
                    if (!path.startsWith("/")) {
                        throw new IllegalAccessException("RequestMapping the value is not startWith '/':" + path);
                    }
                    if (path.endsWith("/")) {
                        throw new IllegalAccessException("RequestMapping the value can not end With '/':" + path);
                    }
                    if (pathMap.containsKey(path)) {
                        throw new IllegalAccessException("RequestMapping has same url value:" + path);
                    }
                    RequestMappingInfo info = new RequestMappingInfo(path, HttpMethod.valueOf(an.method()), permission);
                    pathMap.put(path, info);
                    handlerMethodMap.put(info, handlerMethod);
                } else {
                    String path = an.value();
                    String permission = an.permission();
                    if (!path.startsWith("/")) {
                        throw new IllegalAccessException("RequestMapping the value is not startWith '/':" + path);
                    }
                    if (path.endsWith("/")) {
                        throw new IllegalAccessException("RequestMapping the value can not end With '/':" + path);
                    }
                    if (pathMap.containsKey(path)) {
                        throw new IllegalAccessException("RequestMapping has same url value:" + path);
                    }
                    RequestMappingInfo info = new RequestMappingInfo(path, HttpMethod.valueOf(an.method()), permission);
                    pathMap.put(path, info);
                    handlerMethodMap.put(info, handlerMethod);
                }
            }
        }
    }


    public RequestMappingInfo getRequestMappingInfoByPath(String path) {
        return pathMap.get(path);
    }

    public HandlerMethod getHandlerMethodByRequestMappingInfo(RequestMappingInfo info) {
        return handlerMethodMap.get(info);
    }
}