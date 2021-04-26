package com.fish.netty.udp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author: fjjdragon
 * @date: 2021-03-24 22:02
 */
public class UdpServerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline().addLast(new UDPReceiveHandler());
    }
}