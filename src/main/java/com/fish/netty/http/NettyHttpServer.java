package com.fish.netty.http;

import com.fish.App;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;

/**
 * @author: fjjdragon
 * @date: 2021-03-20 21:23
 */
public class NettyHttpServer extends Thread {

    /**
     * 渠道属性
     */
    public static final AttributeKey<HttpRequest> USER_ID_KEY = AttributeKey.valueOf("user.id");
    /**
     * 服务器是否在running
     */
    public static volatile boolean isRunning = true;

    private static Channel channel;

    @Override
    public void run() {
        onStart();
    }

    private static void onStart() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new HttpServerInitializer());
            if (App.isDebug) {
                b.handler(new LoggingHandler(LogLevel.INFO));
            }
            channel = b.bind(App.getPort()).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            stopServer();
        }
    }

    public static void stopServer() {
        NettyHttpServer.isRunning = false;
        if (channel != null)
            channel.close();
    }
}