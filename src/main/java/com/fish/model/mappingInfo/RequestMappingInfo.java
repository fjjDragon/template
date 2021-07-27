package com.fish.model.mappingInfo;

import io.netty.handler.codec.http.HttpMethod;

/**
 * @author: fjjdragon
 * @date: 2021-07-23 20:02
 */
public class RequestMappingInfo {
    private String path;
    private HttpMethod method;
    private String permission;

    public RequestMappingInfo(String path, HttpMethod method, String permission) {
        this.path = path;
        this.method = method;
        this.permission = permission;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }


    public String getPermission() {
        return permission;
    }

}