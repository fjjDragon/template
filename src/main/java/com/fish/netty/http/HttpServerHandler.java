package com.fish.netty.http;

import com.alibaba.fastjson.JSONObject;
import com.fish.executor.ExecutorService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: fjjdragon
 * @date: 2021-03-20 23:50
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private int method = 0;
    private ByteBuf content = null;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive:" + ctx.channel().remoteAddress().toString());
        content = null;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive:" + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        log.info("messageReceived:{}, class:{}", ctx.channel().remoteAddress().toString(), msg.getClass());
        log.info(msg.toString());
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            // 处理get请求
            if (request.method().equals(HttpMethod.GET)) {
                HttpRequestHandler hr = new HttpRequestHandler(ctx, request);
                hr.setTime(System.currentTimeMillis());
                ExecutorService.execute(hr);
                method = 1;
                return;
            }
            // 判断request请求是否是post请求
            if (request.method().equals(HttpMethod.POST)) {
                method = 2;
                Attribute<HttpRequest> attr = ctx.channel().attr(NettyHttpServer.USER_ID_KEY);
                attr.set(request);
                return;
            }
            method = 0;
            writeMethodNotAllowedRespone(ctx, "Mathod Not Allowed!");
        }
        if (method == 2 && msg instanceof HttpContent) {
            // New chunk is received
            HttpContent chunk = (HttpContent) msg;
            if (content == null) {
                content = Unpooled.buffer();
            }
            content.writeBytes(chunk.content());
            // Log.v("HttpContent********" + content);
            // System.err.println("" + content.readableBytes());
            if (msg instanceof LastHttpContent) {
                Attribute<HttpRequest> attr = ctx.channel().attr(NettyHttpServer.USER_ID_KEY);
                HttpRequest request = attr.get();
                attr.set(null);
                HttpRequestHandler hr = new HttpRequestHandler(ctx, request, content);
                content = null;
                hr.setTime(System.currentTimeMillis());
                ExecutorService.execute(hr);
            }
        } else if (method != 1) {
            writeMethodNotAllowedRespone(ctx, "Mathod Not Allowed!");
        }
    }

    public void writeString(ChannelHandlerContext ctx, String result) {
        ByteBuf buf = null;
        try {
            buf = Unpooled.wrappedBuffer(result.getBytes("UTF-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        response.headers().set("Content-Type", "text/html; charset=UTF-8");
        response.headers().set("Content-Length", buf.readableBytes());

        // Write the response.
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }

    // 返回json格式数据
    public void writeJSONObject(ChannelHandlerContext ctx, JSONObject json, String callBack) {

        ByteBuf buf = null;
        String result;
        if (callBack != null) {
            result = callBack + "(" + json.toString() + ")";
        } else {
            result = json.toString();
        }
        log.info(result);
        try {
            buf = Unpooled.wrappedBuffer(result.getBytes("UTF-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        response.headers().set("Content-Type", "text/html;charset=UTF-8");
        response.headers().set("Content-Length", buf.readableBytes());

        // Write the response.
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }

    /**
     * 不支持协议
     *
     * @param ctx
     * @param message
     */
    public void writeMethodNotAllowedRespone(ChannelHandlerContext ctx, String message) {

        ByteBuf buf = null;
        if (message != null) {
            try {
                buf = Unpooled.wrappedBuffer(message.getBytes("UTF-8"));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.METHOD_NOT_ALLOWED, buf);

        response.headers().set("Content-Type", "text/html; charset=UTF-8");
        response.headers().set("Content-Length", buf.readableBytes());

        // Write the response.
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }

    public void writeNotFound(ChannelHandlerContext ctx) {

        ByteBuf buf = null;
        try {
            buf = Unpooled.wrappedBuffer("Not Found!".getBytes("UTF-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND,
                buf);

        response.headers().set("Content-Type", "text/html; charset=UTF-8");
        response.headers().set("Content-Length", buf.readableBytes());

        // Write the response.
        ctx.channel().writeAndFlush(response);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        log.info("exceptionCaught*******************************" + ctx.channel().remoteAddress());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("read idle");
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("write idle");
                ctx.close();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("all idle");
                ctx.close();
            }
        }
    }

}