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
    private final static String CREATE_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    public static Menu initMenu(){
        Menu menu = new Menu();
        ClickButton button11 = new ClickButton();
        button11.setName("click1菜单");
        button11.setType("click");
        button11.setKey("11");

        ViewButton button21 = new ViewButton();
        button21.setName("view菜单");
        button21.setType("view");
        button21.setUrl("http://www.baidu.com");//我这里测试使用百度网站

        ClickButton button31 = new ClickButton();
        button31.setName("扫码事件");
        button31.setType("scancode_push");
        button31.setKey("31");

        ClickButton button32 = new ClickButton();
        button32.setName("地理位置");
        button32.setType("location_select");
        button32.setKey("32");

        Button button = new Button();
        button.setName("菜单");
        button.setSub_button(new Button[]{button31,button32});

        menu.setButton(new Button[]{button11,button21,button});
        return menu;

    }

    public static int createMenu(String token,String menu) throws IOException {
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", WeChatUtil.getAccessToken().getAccessToken());
        JSONObject jsonObject = JSONObject.parseObject(MyHttpRequest.sendPost(url, menu));
        if(jsonObject != null){
//正常返回0
            result = jsonObject.getIntValue("errcode");
        }
        return result;

    }

    public static AccessToken getAccessToken(){
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s"
                ,"wxaa25b0c4e7ee2590"
                ,"c37b9c300893db3011117f284dee7414");
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
