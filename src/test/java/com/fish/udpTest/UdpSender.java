package com.fish.udpTest;

import com.fish.netty.udp.Frame;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author: fjjdragon
 * @date: 2021-03-24 22:33
 */
public class UdpSender {
    private Channel channel;
    private EventLoopGroup group;
    private Bootstrap b;

    public UdpSender() {
        //第一个线程组是用于接收Client连接的
        group = new NioEventLoopGroup();
        //服务端辅助启动类
        b = new Bootstrap();
        b.group(group)
                //由于我们用的是UDP协议，所以要用NioDatagramChannel来创建
                .channel(NioDatagramChannel.class)
                //向ChannelPipeline里添加业务处理handler
                .handler(new UDPSenderHandler());
        try {
            //不需要建立连接
            channel = b.bind(0).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    public boolean send(String ip, int port, String msg) throws InterruptedException {
        //将UDP请求的报文以DatagramPacket打包发送给接受端
        channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8), new InetSocketAddress(ip, port))).sync();//同步调用
        return true;
    }

    public boolean send(Frame frame) throws InterruptedException {
        channel.writeAndFlush(frame).sync();
        return true;
    }

    /**
     * 关闭释放资源
     */
    public void close() {
        try {
            if (group != null)
                group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        UdpSender udpSender = new UdpSender();
        Scanner sc = new Scanner(System.in);
        for (; ; ) {
            String msg = sc.nextLine();  //读取字符串型输入
            if (msg.equals("exit")) {
                break;
            }
            //            udpSender.send("127.0.0.1", 7788, msg);//单播
            udpSender.send("255.255.255.255", 7788, msg);//广播
        }
        Frame frame = new Frame();
        udpSender.send(frame);

    }
}