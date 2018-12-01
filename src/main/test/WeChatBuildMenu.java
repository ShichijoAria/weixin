import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.yorha.weixin.util.HttpUtils;
import ui.Button;
import ui.Menu;
import ui.ViewButton;

import java.io.IOException;

/**
 *
 * @Description 创建微信自定义菜单
 * @Author       Aria
 * @Date   18-11-18
 * @Project_name weixin
 *
 */
public class WeChatBuildMenu {

    //创建菜单的路径
    private static String CREATE_MENU_URL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /*
     * @Description: 微信自定义菜单
     * @Param: []
     * @return: ui.Menu
     * @Author:  aria
     * @Date: 18-11-25
     */
    public static Menu initMenu(){
        Menu menu = new Menu();

        ViewButton Btn1 = new ViewButton();
        Btn1.setName("身份证识别");
        Btn1.setType("view");
        Btn1.setUrl("http://pi.yorha.org/weixin/IDCard?");

        ViewButton Btn2 = new ViewButton();
        Btn2.setName("驾驶证识别");
        Btn2.setType("view");
        Btn2.setUrl("http://pi.yorha.org/weixin/UploadHandleServlet?");

        ViewButton Btn3 = new ViewButton();
        Btn3.setName("银行卡识别");
        Btn3.setType("view");
        Btn3.setUrl("http://pi.yorha.org/weixin/creditCard?");

        ViewButton Btn4 = new ViewButton();
        Btn4.setName("车牌号识别");
        Btn4.setType("view");
        Btn4.setUrl("http://pi.yorha.org/weixin/carNum?");



        Button button = new Button();
        button.setName("证件识别");
        button.setSub_button(new Button[]{Btn1,Btn2,Btn3,Btn4});

        menu.setButton(new Button[]{button});
        return menu;

    }

    /*
     * @Description:发送请求是自定义菜单生效
     * @Param: [token, menu]
     * @return: int
     * @Author:  aria
     * @Date: 18-11-25
     */
    public static int createMenu(String token,String menu) throws IOException {
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendPost(url, menu));
        if(jsonObject != null){
//正常返回0
            result = jsonObject.getIntValue("errcode");
        }
        return result;

    }

    /**
     *
     *@Description 获取token
     *@Params  [appId, appSecret]
     *@Return  java.lang.String
     *@Author  aria
     *@Date  18-11-26
     *@Throw
     *@Other
     *
     */
    public static String getAccessToken(String appId,String appSecret){
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",appId,appSecret);
        String result = HttpUtils.sendGet(Url);
        System.out.println(result);
        JSONObject json = JSON.parseObject(result);
        return json.getString("access_token");
    }

    public static void main(String[] args) {
        try {
            String token = getAccessToken("wxaa25b0c4e7ee2590","c37b9c300893db3011117f284dee7414");
            /*String filePath = "‪D:/image/123.jpg";
            String mediaId = WenXinUntil.upload(filePath,token.getToken(), "thumb");
            System.out.println("票据"+token.getToken());
            System.out.println("有效时间"+token.getExpiresIn());
            System.out.println(mediaId);*/
            String menu = JSONObject.toJSONString(initMenu());
            int result = createMenu(token, menu);
            if (result == 0) {
                System.out.println("创建菜单成功");
            } else {
                System.out.println("创建菜单失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}