package com.fish;

import com.fish.netty.http.NettyHttpServer;

/**
 * @author: fjjdragon
 * @date: 2021-03-20 21:22
 */
public class App {
    public static final boolean isDebug = true;

    private static int port = 8080;

    public static int getPort() {
        return port;
    }

    public static void main(String[] args) {
        System.out.println("Hello World");


        NettyHttpServer nettyServer = new NettyHttpServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("system shutdown.");
            nettyServer.stopServer();
        }));
        nettyServer.run();

//        NettyUdpServer udpServer = new NettyUdpServer(7788);
//        udpServer.run();


    }


}