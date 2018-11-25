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

        ViewButton Btn1 = new ViewButton();
        Btn1.setName("身份证识别");
        Btn1.setType("view");
        Btn1.setUrl("http://pi.yorha.org/weixin/fileUpload?");//我这里测试使用百度网站

        ViewButton Btn2 = new ViewButton();
        Btn2.setName("驾驶证识别");
        Btn2.setType("view");
        Btn2.setUrl("http://pi.yorha.org/weixin/fileUpload?");//我这里测试使用百度网站

        ViewButton Btn3 = new ViewButton();
        Btn3.setName("银行卡识别");
        Btn3.setType("view");
        Btn3.setUrl("http://pi.yorha.org/weixin/fileUpload?");//我这里测试使用百度网站

        ViewButton Btn4 = new ViewButton();
        Btn4.setName("银行卡识别");
        Btn4.setType("view");
        Btn4.setUrl("http://pi.yorha.org/weixin/fileUpload?");//我这里测试使用百度网站



        Button button = new Button();
        button.setName("证件识别");
        button.setSub_button(new Button[]{Btn1,Btn2,Btn3,Btn4});

        menu.setButton(new Button[]{button});
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
