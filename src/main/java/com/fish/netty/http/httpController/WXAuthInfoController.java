//package com.fish.netty.http.httpController;
//
//import com.alibaba.fastjson.JSONObject;
//import com.fish.annotation.Controller;
//import com.fish.annotation.RequestMapping;
//import com.fish.netty.http.UriDecoder;
//import com.fish.util.WxAuthUtil;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author: fjjdragon
// * @date: 2021-07-26 21:33
// */
//@Slf4j
//@Controller
//@RequestMapping(value = "/auth/")
//public class WXAuthInfoController {
//    @RequestMapping(value = "/wxjscode", method = "POST")
//    public JSONObject getWXUserAuthInfo(UriDecoder hrp) {
//        String code = hrp.getString("code");
//        JSONObject response = WxAuthUtil.getJsCode2Session(WxAuthUtil.APPID, WxAuthUtil.APPSECRET, code);
//        if (response.getIntValue("errcode") == 0) {
//            String openid = response.getString("openid");
//            String unionid = response.getString("unionid");
//            String session_key = response.getString("session_key");
//
//            return null;
//        } else {
//            log.warn("jscode:[" + code + "]," + response.toJSONString());
//            return response;
//        }
//
//    }
//}