package com.fish.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fjjdragon
 * @date: 2021-03-24 22:36
 */
@Slf4j
public class UDPSenderHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        //获得请求
        String req = packet.content().toString(CharsetUtil.UTF_8);
        log.info("Reseive udp response: {}", req);
    }
}