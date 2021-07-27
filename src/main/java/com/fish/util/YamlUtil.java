package com.fish.util;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author: fjjdragon
 * @date: 2021-06-23 12:19
 */
public class YamlUtil {

    /**
     * 从Map中获取配置的值
     * 传的key支持两种形式, 一种是单独的,如user.path.key
     * 一种是获取数组中的某一个,如 user.path.key[0]
     *
     * @param key
     * @return
     */
    public static <T> T getValueByKey(Map<String, Object> properties, String key, T defaultValue) {
        String separator = ".";
        String[] separatorKeys = null;
        if (key.contains(separator)) {
            // 取下面配置项的情况, user.path.keys 这种
            separatorKeys = key.split("\\.");
        } else {
            // 直接取一个配置项的情况, user
            Object res = properties.get(key);
            return res == null ? defaultValue : (T) res;
        }
        // 下面肯定是取多个的情况
        String finalValue = null;
        Object tempObject = properties;
        for (int i = 0; i < separatorKeys.length; i++) {
            //如果是user[0].path这种情况,则按list处理
            String innerKey = separatorKeys[i];
            Integer index = null;
            int idx = innerKey.indexOf("[");
            if (idx > -1) {
                // 如果是user[0]的形式,则index = 0 , innerKey=user
                index = Integer.valueOf(innerKey.substring(idx + 1, innerKey.indexOf("]")));
                innerKey = innerKey.substring(0, idx);
            }
            Map<String, Object> mapTempObj = (Map) tempObject;
            Object object = mapTempObj.get(innerKey);
            // 如果没有对应的配置项,则返回设置的默认值
            if (object == null) {
                return defaultValue;
            }
            Object targetObj = object;
            if (index != null) {
                // 如果是取的数组中的值,在这里取值
                targetObj = ((ArrayList) object).get(index);
            }
            // 一次获取结束,继续获取后面的
            tempObject = targetObj;
            if (i == separatorKeys.length - 1) {
                //循环结束
                return (T) targetObj;
            }
        }
        return null;
    }
}