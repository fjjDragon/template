package com.fish;

import com.fish.common.ServerConfig;
import com.fish.dao.mongo.MongoDao;
import com.fish.dao.redis.RedisDao;
import com.fish.netty.http.NettyHttpServer;
import com.fish.netty.http.RequestMappingHandlerScan;
import com.fish.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fjjdragon
 * @date: 2021-03-20 21:22
 */
@Slf4j
public class App {
    public static final boolean isDebug = true;

    private static int port = 8080;

    public static int getPort() {
        return port;
    }

    public static void main(String[] args) {
        log.warn("loading server config....");
        if (!ServerConfig.getInstance().initConfig()) {
            System.exit(0);
        }

        final NettyHttpServer nettyServer = new NettyHttpServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.warn("system shutdown hook....");
            nettyServer.stopServer();
            HttpRequestUtil.getInstance().close();
            RedisDao.getInstance().shutdown();
            MongoDao.getInstance().close();
        }));

        log.warn("connecting redis....");
        if (!RedisDao.getInstance().init()) {
            System.exit(0);
        }
        log.warn("connecting mongo....");
        if (!MongoDao.getInstance().init()) {
            System.exit(0);
        }
        // 扫描当前包下所有类
        RequestMappingHandlerScan.getInstance().init(App.class.getPackage());


        // 网络监听最后开启
        log.info("start netty....on port(s):" + App.getPort());
        nettyServer.start();


    }


}