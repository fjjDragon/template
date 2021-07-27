package com.fish.netty.http;

import com.alibaba.fastjson.JSONObject;
import com.fish.common.ResponseCode;
import com.fish.model.mappingInfo.HandlerMethod;
import com.fish.model.mappingInfo.RequestMappingInfo;
import com.fish.util.RSAUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

/**
 * @author: fjjdragon
 * @date: 2021-03-21 0:12
 */
@Slf4j
public class HttpRequestHandlerDiapatcher implements BasicHandler {

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

    public HttpRequestHandlerDiapatcher(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public HttpRequestHandlerDiapatcher(ChannelHandlerContext ctx, HttpRequest request, ByteBuf content) {
        this.ctx = ctx;
        this.request = request;
        this.content = content;
    }

    @Override
    public void run() {
        handler();
    }

    @Override
    public void handler() {
        try {
            long cut = System.currentTimeMillis();
            hrp = new UriDecoder();
            hrp.praser(request.uri());
            hrp.setIp(ctx.channel().remoteAddress().toString());
            hrp.setIpFromXForwardFor(request.headers().get("X-Forwarded-For"));
            if (cut - time > 30000l) {
                log.warn("from: [" + hrp.getIp() + "], WAITING : <" + hrp.getPath() + "> " + (cut - time) + "ms ＜OUT OF　TIME＞");
                closeChannel();
                return;
            }
            RequestMappingInfo info = RequestMappingHandlerScan.getInstance().getRequestMappingInfoByPath(hrp.getPath());
            if (info == null || info.getMethod() != request.method()) {
                log.warn("from: [" + hrp.getIp() + "], WAITING : <" + hrp.getPath() + "> <NOT FOUND>");
                writeNotFound(ctx);
                return;
            }
            HandlerMethod handlerMethod = RequestMappingHandlerScan.getInstance().getHandlerMethodByRequestMappingInfo(info);
            if (handlerMethod == null) {
                log.warn("from: [" + hrp.getIp() + "], WAITING : <" + info.getPath() + ">(" + info.getMethod() + ") <METHOD NOT FOUND > ");
                writeNotFound(ctx);
                return;
            }
            if (info.getMethod().equals(HttpMethod.POST)) {
                int offset = content.arrayOffset() + content.readerIndex();
                String body = new String(content.array(), offset, content.readableBytes(), "UTF-8");
                String decrypt = RSAUtil.decrypt(body);
                hrp.parseBodyJSON(decrypt);
            }

            Object obj = handlerMethod.getBeanType().getDeclaredConstructor().newInstance();
            Class<?>[] args = handlerMethod.getMethod().getParameterTypes();
            Object result = null;
            if (args.length == 2) {
                result = handlerMethod.getMethod().invoke(obj, request, hrp);
            } else if (args.length == 1) {
                result = handlerMethod.getMethod().invoke(obj, hrp);
            } else if (args.length == 0) {
                result = handlerMethod.getMethod().invoke(obj);
            }
            if (result == null) {
                writeNotFound(ctx);
                return;
            }
            Type t = handlerMethod.getMethod().getAnnotatedReturnType().getType();
            String typeStr = t.getTypeName();
            if (typeStr.endsWith("String")) {
                writeString(ctx, (String) result, hrp.getString("cb"));
            } else {
                writeJSONObject(ctx, (JSONObject) result, hrp.getString("cb"));
            }

            log.info("FROM:[{}], CT:{}ms,[{}],{}", hrp.getIp(), System.currentTimeMillis() - cut, request.method().toString(), request.uri());
        } catch (Exception e) {
            log.error("", e);
            writeError(ctx, e.getMessage());
            return;
        }
    }

    private static JSONObject DEFALUT_ERROR_JSON = new JSONObject();

    static {
        DEFALUT_ERROR_JSON.put("code", ResponseCode.ERROR.getCode());
        DEFALUT_ERROR_JSON.put("msg", ResponseCode.ERROR.getMsg());
    }

    public void writeError(ChannelHandlerContext ctx, String str) {
        if (null != str) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", ResponseCode.ERROR.getCode());
            jsonObject.put("msg", str);
            writeJSONObject(ctx, jsonObject, null);
        } else {
            writeJSONObject(ctx, DEFALUT_ERROR_JSON, null);
        }
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

    public void writeNotFound(ChannelHandlerContext ctx) {
        ByteBuf buf = null;
        try {
            buf = Unpooled.wrappedBuffer("404 Not Found!".getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, buf);
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