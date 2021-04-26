package com.fish.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fjjdragon
 * @date: 2021-03-24 21:55
 */
@Slf4j
public class NettyUdpServer extends Thread {

    /**
     * 服务器是否在running
     */
    public static volatile boolean isRunning = true;
    private static NioEventLoopGroup group;
    private static Channel channel;
    private static int port;

    public NettyUdpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        onStart();
    }

//    private static void onStart() {
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new Bootstrap();
//            b.group(group).channel(NioDatagramChannel.class);
//            b.childHandler(new HttpServerInitializer());
//            if (App.isDebug) {
//                b.handler(new LoggingHandler(LogLevel.INFO));
//            }
//            channel = b.bind(App.getPort()).sync().channel();
//            channel.closeFuture().sync();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            group.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//            stopServer();
//        }
//    }

    private static void onStart() {
        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
//                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpServerInitializer());
            //没有接受客户端连接的过程，监听本地端口即可
            ChannelFuture sync = bootstrap.bind(port).sync();
            log.info("UDP服务器启动成功");
            //绑定 Channel
            long count = 0;
            channel = sync.channel();
            ChannelFuture sync1 = channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("", e);
            close();
        }
    }

    public static void close() {

        try {
            if (null != channel) {
                channel.closeFuture().sync();
            }
            if (null != group)
                group.shutdownGracefully().sync();
        } catch (Exception e) {
            log.error("", e);
        }
    }

}