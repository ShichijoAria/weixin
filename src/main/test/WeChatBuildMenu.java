import com.alibaba.fastjson.JSONObject;
import org.yorha.weixin.entity.AccessToken;
import org.yorha.weixin.util.WeChatUtil;

public class WeChatBuildMenu {
    public static void main(String[] args) {
        try {
            AccessToken token = WeChatUtil.getAccessToken();
            /*String filePath = "‪D:/image/123.jpg";
            String mediaId = WenXinUntil.upload(filePath,token.getToken(), "thumb");
            System.out.println("票据"+token.getToken());
            System.out.println("有效时间"+token.getExpiresIn());
            System.out.println(mediaId);*/
            String menu = JSONObject.toJSONString(WeChatUtil.initMenu());
            int result = WeChatUtil.createMenu(token.getAccessToken(), menu);
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