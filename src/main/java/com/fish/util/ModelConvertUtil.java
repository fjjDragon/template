package com.fish.util;

import com.fish.annotation.ClassField;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ModelConvertUtil {

    /**
     * 参数列表map转化为model
     *
     * @param clazz
     * @param params
     * @param <A>
     * @return
     */
    public static <A extends Object> A convertToModel(Class<A> clazz, Map<String, String> params) {
        A model = null;
        try {
            model = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("", e);
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Type type = field.getGenericType();
            String typeStr = type.toString();
            String fieldName = field.getName();
            if (!params.containsKey(fieldName)) {
                continue;
            }
            String value = params.get(fieldName);
            try {
                if (typeStr.endsWith("String")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Integer.class);
                    method.invoke(model, value);
                } else if (typeStr.endsWith("Integer") || typeStr.equals("int")) {

                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Integer.class);
                    Integer intValue = null;
                    try {
                        intValue = Integer.valueOf(value);
                    } catch (Exception e) {
                        return null;
                    }
                    method.invoke(model, intValue);
                } else if (typeStr.endsWith("Double") || typeStr.equals("double")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Double.class);
                    Double doubleValue = null;
                    try {
                        doubleValue = Double.valueOf(value);
                    } catch (Exception e) {
                        return null;
                    }
                    method.invoke(model, doubleValue);
                } else if (typeStr.endsWith("Boolean") || typeStr.equals("boolean")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Boolean.class);
                    Boolean aBoolean = null;
                    try {
                        aBoolean = Boolean.valueOf(value);
                    } catch (Exception e) {
                        return null;
                    }
                    method.invoke(model, aBoolean);
                } else if (typeStr.endsWith("Long") || typeStr.equals("long")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Long.class);
                    Long aLong = null;
                    try {
                        aLong = Long.valueOf(value);
                    } catch (Exception e) {
                        return null;
                    }
                    method.invoke(model, aLong);
                } else if (typeStr.equals("java.util.List<java.lang.String>")) {
                    String[] values = value.split("_");
                    List<String> list = new ArrayList<>();
                    for (String v : values) {
                        list.add(v);
                    }
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), List.class);
                    method.invoke(model, method);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return model;
    }

    public static <T extends Object> List<T> convertToModel(Class<T> clazz, List<Document> list) {
        List<T> models = new ArrayList<>();
        if (list == null || list.size() <= 0) {
            return models;
        }
        for (Document doc : list) {
            models.add(convertToModel(clazz, doc));
        }
        return models;
    }

    /**
     * Document对象转为实体类
     */
    public static <A extends Object> A convertToModel(Class<A> clazz, Document info) {
        if (info == null) {
            return null;
        }
        A model = null;
        try {
            model = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("", e);
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ClassField temp = field.getAnnotation(ClassField.class);
            Type type = field.getGenericType();
            String typeStr = type.toString();
            String fieldName = field.getName();
            String key;
            if (temp != null) {
                if (!temp.serialize()) continue;
                key = temp.value();
            } else {
                key = fieldName;
            }
            try {
                if (null == info.get(key))
                    continue;
                if (typeStr.endsWith("String")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), String.class);
                    method.invoke(model, info.getString(key));
                } else if (typeStr.endsWith("Integer") || typeStr.equals("int")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Integer.class);
                    Integer value = null;
                    try {
                        value = info.getInteger(key);
                    } catch (Exception e) {
                    }
                    method.invoke(model, value);
                } else if (typeStr.endsWith("Double") || typeStr.equals("double")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Double.class);
                    Double value = null;
                    try {
                        value = info.getDouble(key);
                    } catch (Exception e) {
                    }
                    method.invoke(model, value);
                } else if (typeStr.endsWith("Boolean") || typeStr.equals("boolean")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Boolean.class);
                    Boolean value = null;
                    try {
                        value = info.getBoolean(key);
                    } catch (Exception e) {
                    }
                    method.invoke(model, value);
                } else if (typeStr.endsWith("Long") || typeStr.equals("long")) {
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), Long.class);
                    Long value = null;
                    try {
                        value = info.getLong(key);
                    } catch (Exception e) {
                    }
                    method.invoke(model, value);
                } else if (typeStr.equals("java.util.List<java.lang.String>")) {
                    List<String> list = info.get(key, ArrayList.class);
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), List.class);
                    method.invoke(model, list);
                } else if (typeStr.equals("java.util.List<java.lang.Integer>")) {
                    List<Integer> list = info.get(key, ArrayList.class);
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), List.class);
                    method.invoke(model, list);
                } else if (typeStr.equals("java.util.List<org.bson.Document>")) {
                    List<Document> list = info.get(key, ArrayList.class);
                    if (null == list) continue;
                    Method method = clazz.getDeclaredMethod("set" + firstChar2UpperCase(fieldName), List.class);
                    method.invoke(model, list);
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return model;
    }

    /**
     * 实体类转为Document对象
     *
     * @param object
     * @return
     */
    public static Document converToDocument(Object object) {
        if (object == null) {
            return null;
        }
        Document doc = new Document();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ClassField temp = field.getAnnotation(ClassField.class);
            Type type = field.getGenericType();
            String typeStr = type.toString();
            String fieldName = field.getName();
            String key;
            if (temp != null) {
                if (!temp.serialize()) continue;
                key = temp.value();
            } else {
                key = fieldName;
            }
            try {
                Method method = clazz.getDeclaredMethod("get" + firstChar2UpperCase(fieldName));
                if (typeStr.endsWith("String")) {
                    String result = (String) method.invoke(object);
                    if (result != null) {
                        doc.put(key, result);
                    }
                } else if (typeStr.endsWith("Integer") || typeStr.equals("int")) {
                    Integer result = (Integer) method.invoke(object);
                    if (result != null) {
                        doc.put(key, result);
                    }
                } else if (typeStr.endsWith("Double") || typeStr.equals("double")) {
                    Double result = (Double) method.invoke(object);
                    if (result != null) {
                        doc.put(key, result);
                    }
                } else if (typeStr.endsWith("Boolean") || typeStr.equals("boolean")) {
                    Boolean result = (Boolean) method.invoke(object);
                    if (result != null) {
                        doc.put(key, result);
                    }
                } else if (typeStr.endsWith("Long") || typeStr.equals("long")) {
                    Long result = (Long) method.invoke(object);
                    if (result != null) {
                        doc.put(key, result);
                    }
                } else if (typeStr.equals("java.util.List<java.lang.String>")) {
                    List<String> result = (ArrayList<String>) method.invoke(object);
                    if (result != null) {
                        doc.put(key, result);
                    }
                } else if (typeStr.equals("java.util.List<java.lang.Integer>")) {
                    @SuppressWarnings("unchecked")
                    List<Integer> result = (ArrayList<Integer>) method.invoke(object);
                    if (result != null) {
                        doc.put(key, result);
                    }
                } else if (typeStr.equals("java.util.List<org.bson.Document>")) {
                    List<Document> result = (ArrayList<Document>) method.invoke(object);
                    if (result != null) {
                        doc.put(key, result);
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        return doc;
    }

    /**
     * 首字母转大写
     *
     * @param str
     * @return
     */
    private static String firstChar2UpperCase(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            if (ch[1] >= 'A' && ch[1] <= 'Z') {
                ch[0] = ch[0];
            } else {
                ch[0] = (char) (ch[0] - 32);
            }
        }
        return new String(ch);
    }

}
