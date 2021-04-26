package com.fish.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author: fjjdragon
 * @date: 2021-03-20 21:26
 */
public class HttpServerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 服务器超时监听
        pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 30, 60));
        // http服务器端对request解码
        pipeline.addLast(new HttpRequestDecoder());
        // http服务器端对response编码
        pipeline.addLast(new HttpResponseEncoder());
        // 目的是支持异步大文件传输
        // pipeline.addLast(new ChunkedWriteHandler());
        /**
         * 压缩 Compresses an HttpMessage and an HttpContent in gzip or deflate
         * encoding while respecting the "Accept-Encoding" header. If there is
         * no matching encoding, no compression is done. 0-9压缩比例
         */
        // pipeline.addLast(new HttpContentCompressor(5));
        // 处理请求
        pipeline.addLast(new HttpServerHandler());
    }
}