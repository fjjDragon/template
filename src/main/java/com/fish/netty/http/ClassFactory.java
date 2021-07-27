//package com.fish.netty.http;
//
//import com.fish.common.ServerConfig;
//import com.fish.netty.http.httpController.AstractHttpController;
//import com.fish.util.YamlUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.lang.reflect.Method;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Stack;
//
///**
// * @author: fjjdragon
// * @date: 2021-07-23 17:19
// */
//@Slf4j
//public class ClassFactory {
//    public static ClassFactory getInstance() {
//        return ClassFactory.ClassFactoryHolder.instance;
//    }
//
//    static class ClassFactoryHolder {
//        private static ClassFactory instance = new ClassFactory();
//    }
//
//    private ClassFactory() {
//    }
//
//    private boolean UseDynamicClasses = YamlUtil.getValueByKey(ServerConfig.getInstance().getConfigProperties(), "server.UseDynamicClasses", false);
//
//
//    /**
//     * 接口名称和类的对应关系
//     */
//    private HashMap<String, String> IntefaceClassNameMap = new HashMap<>();
//
//    /**
//     * 路由和内存类的对应关系
//     */
//    private HashMap<String, Class<BasicHandler>> ClassFactoryMap;
//
//    /**
//     * 重新加载动态类名和路由关系
//     */
//    private HashMap<String, String> reloadClassFactory() {
//        HashMap<String, String> tempMap = new HashMap<>();
//        long time = System.currentTimeMillis();
//
//
//        time = System.currentTimeMillis() - time;
//        log.info("reloadClassFactory CST : " + time + "ms");
//
//        IntefaceClassNameMap = tempMap;
//        return IntefaceClassNameMap;
//    }
//
//    /**
//     * 加载类集
//     *
//     * @return
//     */
//    public static boolean reloadDynamicClass() {
//        HashMap<String, String> IntefaceClassNameHm = reloadClassFactory();
//        if (IntefaceClassNameHm == null) {
//            return false;
//        }
//
//        if (ServerConfig.UseDynamicClasses == 1) {
//            long time = System.currentTimeMillis();
//            HashMap<String, Class<AstractHttpController>> hm = new HashMap<String, Class<AstractHttpController>>();
//            try {
//                // 设置class文件所在根路径
//                // 例如/usr/java/classes下有一个test.App类，则/usr/java/classes即这个类的根路径，而.class文件的实际位置是/usr/java/classes/test/App.class
//                File clazzPath = new File(ServerConfig.DynamicClassesPath);
//
//                // 记录加载.class文件的数量
//                int clazzCount = 0;
//
//                if (clazzPath.exists() && clazzPath.isDirectory()) {
//                    // 获取路径长度
//                    int clazzPathLen = clazzPath.getAbsolutePath().length() + 1;
//
//                    Stack<File> stack = new Stack<>();
//                    stack.push(clazzPath);
//
//                    // 遍历类路径
//                    while (stack.isEmpty() == false) {
//                        File path = stack.pop();
//                        File[] classFiles = path.listFiles(new FileFilter() {
//                            public boolean accept(File pathname) {
//                                return pathname.isDirectory() || pathname.getName().endsWith(".class");
//                            }
//                        });
//                        for (File subFile : classFiles) {
//                            if (subFile.isDirectory()) {
//                                stack.push(subFile);
//                            } else {
//                                if (clazzCount++ == 0) {
//                                    Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
//                                    boolean accessible = method.isAccessible();
//                                    try {
//                                        if (accessible == false) {
//                                            method.setAccessible(true);
//                                        }
//                                        // 设置类加载器
//                                        URLClassLoader classLoader = (URLClassLoader) ClassLoader
//                                                .getSystemClassLoader();
//                                        // 将当前类路径加入到类加载器中
//                                        method.invoke(classLoader, clazzPath.toURI().toURL());
//                                    } finally {
//                                        method.setAccessible(accessible);
//                                    }
//                                }
//                                // 文件名称
//                                String className = subFile.getAbsolutePath();
//                                className = className.substring(clazzPathLen, className.length() - 6);
//                                className = className.replace(File.separatorChar, '.');
//                                // 加载Class类
//                                Class<AstractHttpController> CI = (Class<AstractHttpController>) Class.forName(className);
//                                hm.put(className, CI);
//                                log.v("读取应用程序类文件 " + className);
//                            }
//                        }
//                    }
//                }
//                HashMap<String, Class<AstractHttpController>> ClassFactoryHm = new HashMap<String, Class<AstractHttpController>>();
//                Iterator<Map.Entry<String, String>> iter = IntefaceClassNameHm.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry<String, String> entry = iter.next();
//                    String key = entry.getKey();
//                    String val = entry.getValue();
//                    Class<AstractHttpController> CI = hm.get(val);
//                    ClassFactoryHm.put(key, CI);
//                }
//
//                setClassFactoryHm(ClassFactoryHm);
//                setIntefaceClassNameHm(IntefaceClassNameHm);
//                time = System.currentTimeMillis() - time;
//                log.info("reloadDynamicClass CST : " + time + "ms");
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        } else {
//            long time = System.currentTimeMillis();
//            HashMap<String, Class<AstractHttpController>> ClassFactoryHm = new HashMap<String, Class<AstractHttpController>>();
//            Iterator<Map.Entry<String, String>> iter = IntefaceClassNameHm.entrySet().iterator();
//            while (iter.hasNext()) {
//                Map.Entry<String, String> entry = iter.next();
//                String key = entry.getKey();
//                String val = entry.getValue();
//                try {
//                    Class<AstractHttpController> CI = (Class<AstractHttpController>) Class.forName(val);
//                    ClassFactoryHm.put(key, CI);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            setClassFactoryHm(ClassFactoryHm);
//            setIntefaceClassNameHm(IntefaceClassNameHm);
//            time = System.currentTimeMillis() - time;
//            log.info("reloadClass CST : " + time + "ms");
//        }
//        return true;
//    }
//
//    public static HashMap<String, String> getIntefaceClassNameHm() {
//        return IntefaceClassNameMap;
//    }
//
//    public static void setIntefaceClassNameHm(HashMap<String, String> intefaceClassNameHm) {
//        IntefaceClassNameMap = intefaceClassNameHm;
//    }
//
//    public static HashMap<String, Class<AstractHttpController>> getClassFactoryHm() {
//        return ClassFactoryMap;
//    }
//
//    public static void setClassFactoryHm(HashMap<String, Class<AstractHttpController>> classFactoryHm) {
//        ClassFactoryMap = classFactoryHm;
//    }
//}