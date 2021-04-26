package com.fish.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author: fjjdragon
 * @date: 2021-03-21 0:12
 */
public class HttpRequestHandler extends BasicHandler {

    public HttpRequestHandler(ChannelHandlerContext ctx, HttpRequest request) {
        super(ctx, request);
    }

    public HttpRequestHandler(ChannelHandlerContext ctx, HttpRequest request, ByteBuf content) {
        super(ctx, request, content);
    }

    @Override
    public void doGet() {

    }

    @Override
    public void doPost() {

    }
}