package com.fish.netty.http.httpController;

import com.alibaba.fastjson.JSONObject;
import com.fish.annotation.Controller;
import com.fish.annotation.RequestMapping;
import com.fish.common.LoginType;
import com.fish.common.ResponseCode;
import com.fish.dao.mongo.MongoDBOperation;
import com.fish.netty.http.UriDecoder;
import com.fish.util.WxAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

/**
 * @author: fjjdragon
 * @date: 2021-07-26 18:02
 */
@Slf4j
@Controller
@RequestMapping(value = "/game/login")
public class LoginController {

    /**
     * 查询闪电玩游戏
     *
     * @param hrp
     * @return
     */
    @RequestMapping(value = "/wxjscode", method = "POST")
    public JSONObject wxJsCode(UriDecoder hrp) {
        JSONObject body = hrp.getBodyJSON();
        String code = body.getString("jscode");
        String appid = body.getString("appid");
        String nick = body.getString("nickName");
        String avatar = body.getString("avatarUrl");
        int sex = body.getInteger("gender");
        String province = body.getString("province");
        String city = body.getString("city");
        String country = body.getString("country");
        int rfid = 0;
        if (body.containsKey("rfid")) {
            rfid = body.getInteger("rfid");
        }
        String from = body.getString("from");
        String fromAppId = body.getString("fromAppId");

        int loginType = LoginType.WX_AUTH.getValue();
        JSONObject response = WxAuthUtil.getJsCode2Session(WxAuthUtil.APPID, WxAuthUtil.APPSECRET, code);
        if (response == null || !response.containsKey("openid")) {
            JSONObject jsonObject = ResponseCode.ERROR.toJSONObject();
            jsonObject.put("theThird", response);
            return jsonObject;
        }

        if (response.getIntValue("errcode") != 0) {
            log.warn("jscode:[" + code + "]," + response.toJSONString());
            return response;
        } else {
            String openid = response.getString("openid");
            String unionid = response.getString("unionid");
            String session_key = response.getString("session_key");


//            if (oneAndUpdate == null || !oneAndUpdate.containsKey("phone") ||) ==null){// 判断是首次绑定,新增积分
//                return null;
//            }

            JSONObject jsonObject = new JSONObject();
            return jsonObject;
        }

    }

    /**
     * 更新保存邀请人信息
     *
     * @param rrd
     * @param channelId
     * @param id
     * @param loginType
     */
    private void updateRfIdInfo(MongoDBOperation mongo, String rrd, int channelId, long id, int loginType) {
        long rfId = 0L;
        try {
            rfId = Long.valueOf(rrd);
        } catch (Exception e) {
        }
        if (rfId == id) {
            return;
        }
        Document cdn = new Document();
        cdn.put("id", id);
        Document obj = new Document();
        obj.put("rfId", rfId);
        obj.put("channel", channelId);
        obj.put("fl", loginType);
        obj.put("time", System.currentTimeMillis());
//        mongo.updateRfIdInfo(cdn, new Document("$setOnInsert", obj), true);
    }
}