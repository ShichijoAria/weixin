package org.yorha.weixin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.yorha.weixin.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String result = MyHttpRequest.sendGet(Url);
        System.out.println(result);
        //response.getWriter().println(result);
        JSONObject json = JSON.parseObject(result);
        AccessToken token = new AccessToken();
        token.setAccessToken(json.getString("access_token"));
        token.setExpiresin(json.getInteger("expires_in"));
        return token;
    }

}
