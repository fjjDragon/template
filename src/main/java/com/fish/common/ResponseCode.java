package com.fish.common;

import com.alibaba.fastjson.JSONObject;

public enum ResponseCode {

    OK(1, "success"),

    ERROR(0, "error"),
    ERROR_TOKEN_EXPIRED(1001, "token过期"),
    ERROR_TOKEN(1002, "账户登陆信息异常"),
    ERROR_OVERTIME(1003, "超时，请重新登陆"),
    ERROR_SIGN_ERROR(1101, "签名验证失败"),
    ERROR_USERINFO(1110, "用户信息异常"),
    ERROR_CONFIG_INFO(1201, "请求数据与配置信息不符"),
    ERROR_CONFIG_GOLD(1205, "金币不足");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", this.code);
        jsonObject.put("msg", this.msg);
        return jsonObject;
    }

}
