package com.fish.netty.http;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fjjdragon
 * @date: 2021-03-20 16:06
 */
@Slf4j
public abstract class BasicHandler implements Runnable {
    /**
     * http请求的上下文
     */
    protected ChannelHandlerContext ctx;
    /**
     * http请求数据
     */
    protected HttpRequest request;
    /**
     * 解析url上的参数
     */
    protected UriDecoder hrp;
    /**
     * http post 数据内容
     */
    protected ByteBuf content;
    /**
     * 记录每条消息的接收时间
     */
    protected long time;

    public BasicHandler(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public BasicHandler(ChannelHandlerContext ctx, HttpRequest request, ByteBuf content) {
        this.ctx = ctx;
        this.request = request;
        this.content = content;
    }

    /**
     * 处理GET请求
     */
    public abstract void doGet();

    /**
     * 处理POST请求
     */
    public abstract void doPost();

    @Override
    public void run() {
        long cut = System.currentTimeMillis();
        String remoteIp = ctx.channel().remoteAddress().toString();
        hrp = new UriDecoder();
        hrp.praser(request.uri());
//        hrp.setIp(remoteIp);
//        hrp.setIpFromXForwardFor(request.headers().get("X-Forwarded-For"));
        if (cut - time > 30000l) {
            log.warn("from: [{}], WAITING : {}ms ＜OUT OF　TIME＞", remoteIp, (cut - time));
            ctx.channel().close();
            return;
        }
//        if (request.method() == HttpMethod.GET)
//            doGet();
//        else
//            doPost();
//        cut = System.currentTimeMillis() - cut;
//        log.info("from: [{}], CT: {}ms, [{}]  {}", hrp.getIp(), cut, request.method().toString(), request.uri());
    }

    /**
     * 输出字符串数据
     *
     * @param ctx
     * @param result
     * @param callBack
     */
    public void writeString(ChannelHandlerContext ctx, String result, String callBack) {
        ByteBuf buf = null;
        if (callBack != null) {
            result = callBack + "(" + result + ")";
        }
        try {
            buf = Unpooled.wrappedBuffer(result.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        response.headers().set("Content-Type", "application/json; charset=UTF-8");
        response.headers().set("Content-Length", response.content().readableBytes());
        response.headers().set("Access-Control-Allow-Origin", "*");
        // Write the response.
        ctx.writeAndFlush(response);
        // 不能主动close，会导致大数据情况下，没法发送完整
        // ctx.close();
    }

    /**
     * 输出json格式数据
     *
     * @param ctx
     * @param json
     * @param callBack
     */
    public void writeJSONObject(ChannelHandlerContext ctx, JSONObject json, String callBack) {
        writeString(ctx, json.toString(), callBack);
    }

    public void writeNotFound(ChannelHandlerContext ctx) {
        ByteBuf buf = null;
        try {
            buf = Unpooled.wrappedBuffer("404 Not Found!".getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
                buf);
        response.headers().set("Content-Type", "application/json; charset=UTF-8");
        response.headers().set("Content-Length", buf.readableBytes());
        response.headers().set("Access-Control-Allow-Origin", "*");
        // Write the response.
        ctx.writeAndFlush(response);
        ctx.close();
    }

    /**
     * 断开连接
     */
    public void closeChannel() {
        ctx.close();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}