package org.yorha.weixin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.yorha.weixin.entity.*;

import java.io.IOException;

/**
 * @Description
 * @Author aria
 * @Date 18-11-24
 * @Project_name weixin
 */
public class WeChatUtil {

    private static String appId;
    private static String appSecret;

    static {
        try {
            appSecret = ResourceUtil.getProperty("config","appSecret");
            appId = ResourceUtil.getProperty("config","appId");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * @Description: 获取token方法
     * @Param: []
     * @return: org.yorha.weixin.entity.AccessToken
     * @Author:  aria
     * @Date: 18-11-25
     */
    public static AccessToken getAccessToken(){
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",appId,appSecret);
        String result = HttpUtils.sendGet(Url);
        System.out.println(result);
        //response.getWriter().println(result);
        JSONObject json = JSON.parseObject(result);
        AccessToken token = new AccessToken();
        token.setAccessToken(json.getString("access_token"));
        token.setExpiresin(json.getInteger("expires_in"));
        return token;
    }

}
