package com.fish.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnoTationUtil {

    /**
     * 类是否被注解
     *
     * @param clazz
     * @param annotation
     * @return
     */
    public static boolean isAnnotationDeclaringClass(Class<?> clazz, Class<?> annotation) {
        Annotation[] anns = clazz.getDeclaredAnnotations();
        for (Annotation ann : anns) {
            if (ann.annotationType() == annotation) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法是否被注解
     *
     * @param method
     * @param annotation
     * @return
     */
    public static boolean isAnnotationDeclaringMethod(Method method, Class<?> annotation) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation ann : annotations) {
            if (ann.annotationType() == annotation) {
                return true;
            }
        }
        return false;
    }

    public static <A extends Annotation> A findAnnotationDeclaringClass(Class<?> clazz, Class<A> annotation) {
        return clazz.getAnnotation(annotation);
    }

    public static <A extends Annotation> A findAnnotationDeclaringMethod(Method method, Class<A> annotation) {
        return method.getAnnotation(annotation);
    }
}
