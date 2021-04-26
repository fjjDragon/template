package com.fish.netty.udp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fjjdragon
 * @date: 2021-03-24 22:09
 */
@Slf4j
public class UDPReceiveHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        //获得请求
        String req = msg.content().toString(CharsetUtil.UTF_8);
        log.info("Receive udp msg: {}", req);
        /**
         *   应答消息也使用DatagramPacket对象，通过packet.sender() 来获取发送者的地址。
         */
        DatagramPacket response = null;
        if (req.equals("hello")) {
            response = new DatagramPacket(Unpooled.copiedBuffer("world", CharsetUtil.UTF_8), msg.sender());
        } else {
            response = new DatagramPacket(Unpooled.copiedBuffer(req, CharsetUtil.UTF_8), msg.sender());
        }

        if (null != response)
            ctx.writeAndFlush(response);

    }
}