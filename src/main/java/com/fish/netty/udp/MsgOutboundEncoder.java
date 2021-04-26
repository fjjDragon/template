package com.fish.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author: fjjdragon
 * @date: 2021-03-24 22:56
 */
public class MsgOutboundEncoder extends MessageToMessageEncoder<Frame> {
    private final InetSocketAddress remoteAddress;

    public MsgOutboundEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Frame frame, List out) throws Exception {
        byte[] msg = frame.getBytes();
        //容量的计算：两个long型+消息的内容+分割符
        ByteBuf buf = channelHandlerContext.alloc()
                .buffer(32 + msg.length + 1);
        //将一个拥有数据和目的地地址的新 DatagramPacket 添加到出站的消息列表中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}