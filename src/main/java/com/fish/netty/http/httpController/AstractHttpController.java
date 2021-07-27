package com.fish.netty.http.httpController;

import com.alibaba.fastjson.JSONObject;
import com.fish.netty.http.UriDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author: fjjdragon
 * @date: 2021-07-23 17:43
 */
public abstract class AstractHttpController {

    /**
     * 处理get请求的逻辑
     *
     * @param request
     * @param hrp
     * @return
     */
    public abstract JSONObject runGetOption(HttpRequest request, UriDecoder hrp);

    /**
     * 处理post请求的逻辑
     *
     * @param request
     * @param hrp
     * @param content
     * @return
     */
    public abstract JSONObject runPostOption(HttpRequest request, UriDecoder hrp, ByteBuf content);
}