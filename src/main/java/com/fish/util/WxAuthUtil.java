package com.fish.util;

import com.alibaba.fastjson.JSONObject;

public class WxAuthUtil {
    public final static String APPID = "wx0311723384f91b6c";
    public final static String APPSECRET = "72208176af7a2bc1026b21f4adbc2fbc";

    /**
     * 通过code获取access_token
     */
    public final static String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 链接进行refresh_token：
     */
    public final static String REFRESH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    /**
     * 检验授权凭证（access_token）是否有效
     */
    public final static String CHECK_ACCESS_TOEKN = "https://api.weixin.qq.com/sns/auth";
    /**
     * 获取用户的资料信息
     */
    public final static String GET_WX_USER_INFO = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 微信小游戏和小程序，code2Session
     */
    public final static String WX_JSCODE_CHECK_URL = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 手机QQ小游戏和小程序验证code有效接口
     */
    public final static String SHOUQ_JSCODE_CHECK_URL = "https://api.q.qq.com/sns/jscode2session";

    /**
     * Descrition: appid 是 应用唯一标识，在微信开放平台提交应用审核通过后获得 secret 是
     * 应用密钥AppSecret，在微信开放平台提交应用审核通过后获得 code 是 填写第一步获取的code参数 grant_type 是
     * 填authorization_code
     *
     * @return access_token 接口调用凭证 expires_in access_token接口调用凭证超时时间，单位（秒）
     * refresh_token 用户刷新access_token openid 授权用户唯一标识 scope
     * 用户授权的作用域，使用逗号（,）分隔
     */
    public static JSONObject getAccessToken(String appid, String secret, String code) {
        // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String url = GET_ACCESS_TOKEN + "?appid=" + appid + "&secret=" + secret + "&code=" + code
                + "&grant_type=authorization_code";
        return HttpRequestUtil.getInstance().doGet(url);
    }

    /**
     * Descrition:access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，
     * 当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种： 1.
     * 若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
     * 2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，
     * 相当于续期access_token。refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权，
     * 所以，请开发者在refresh_token即将过期时（如第29天时），进行定时的自动刷新并保存好它。
     *
     * @param appid
     * @returnaccess_token 接口调用凭证 expires_in access_token接口调用凭证超时时间，单位（秒）
     * refresh_token 用户刷新access_token openid 授权用户唯一标识 scope
     * 用户授权的作用域，使用逗号（,）分隔
     */
    public static JSONObject refreshAccessToken(String appid, String refresh_token) {
        // https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
        String url = REFRESH_ACCESS_TOKEN + "?appid=" + appid + "&refresh_token=" + refresh_token
                + "&grant_type=refresh_token";
        return HttpRequestUtil.getInstance().doGet(url);
    }

    /**
     * Descrition:检验授权凭证（access_token）是否有效
     *
     * @return 正确的Json返回结果： { "errcode":0,"errmsg":"ok" }
     */
    public static JSONObject checkAccessToken(String openid, String access_token) {
        // https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID
        String url = CHECK_ACCESS_TOEKN + "?openid=" + openid + "&access_token=" + access_token;
        return HttpRequestUtil.getInstance().doGet(url);
    }

    /**
     * Descrition:此接口用于获取用户个人信息。开发者可通过OpenID来获取用户基本信息。特别需要注意的是，如果开发者拥有多个移动应用、
     * 网站应用和公众帐号，可通过获取用户基本信息中的unionid来区分用户的唯一性，因为只要是同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，
     * 用户的unionid是唯一的。换句话说，同一用户，对同一个微信开放平台下的不同应用，unionid是相同的。请注意，在用户修改微信头像后，
     * 旧的微信头像URL将会失效，因此开发者应该自己在获取用户信息后，将头像图片保存下来，避免微信头像URL失效后的异常情况。
     *
     * @param openid
     * @param access_token
     * @returnopenid 普通用户的标识，对当前开发者帐号唯一 nickname 普通用户昵称 sex 普通用户性别，1为男性，2为女性
     * province 普通用户个人资料填写的省份 city 普通用户个人资料填写的城市 country 国家，如中国为CN
     * headimgurl
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
     * 用户没有头像时该项为空 privilege 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     * unionid 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */
    public static JSONObject getWxUserInfo(String openid, String access_token) {
        // https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        String url = GET_WX_USER_INFO + "?lang=zh_CN&openid=" + openid + "&access_token=" + access_token;
        return HttpRequestUtil.getInstance().doGet(url);
    }

    /**
     * 属性	类型	说明
     * openid	string	用户唯一标识
     * session_key	string	会话密钥
     * unionid	string	用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
     * errcode	number	错误码
     * errmsg	string	错误信息
     * <p>
     * errcode 的合法值:
     * <hd>值</hd>	说明	最低版本
     * -1	系统繁忙，此时请开发者稍候再试
     * 0	请求成功
     * 40029	code 无效
     * 45011	频率限制，每个用户每分钟100次
     * 40226	高风险等级用户，小程序登录拦截 。风险等级详见用户安全解方案
     *
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public static JSONObject getJsCode2Session(String appid, String secret, String code) {
        String url = WX_JSCODE_CHECK_URL + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code
                + "&grant_type=authorization_code";
        return HttpRequestUtil.getInstance().doGet(url);
    }

    public static JSONObject getShouQJsCode2Session(String appid, String secret, String code) {
        String url = SHOUQ_JSCODE_CHECK_URL + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code
                + "&grant_type=authorization_code";
        return HttpRequestUtil.getInstance().doGet(url);
    }

    /**
     * 从WXPushServer统一凭证管理服务获取AccessToken
     *
     * @return
     */
    public static String getWXPushPayCenterAccessToken(String appid, String sdwGameId, String key) {
        long sec = System.currentTimeMillis();
        String sign = MD5Util.MD5("" + sdwGameId + sec + key);
        String url = "https://wxpush.shandw.com/getaccesstoken/" + appid + "?appid=" + sdwGameId + "&sec=" + sec + "&sign=" + sign;
        JSONObject result = HttpRequestUtil.getInstance().doGet(url);
        if (result != null && result.containsKey("AccessToken")) {
            return result.getString("AccessToken");
        }
        return null;
    }
}
