package org.yorha.weixin.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 消息处理工具类
 * @Author aria
 * @Date 18-11-18
 * @Project_name weixin
 */
public class MessageHandlerUtil {

    /**
     * @Description 解析微信发来的请求（XML）
     * @Params [request] 封装了请求信息的HttpServletRequest对象
     * @Return java.util.Map<java.lang.String   ,   java.lang.String>
     * @Author aria
     * @Date 18-11-26
     * @Throw Exception
     * @Other
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            System.out.println(e.getName() + "|" + e.getText());
            map.put(e.getName(), e.getText());
        }

        // 释放资源
        inputStream.close();
        return map;
    }


    /**
     * @Description 根据消息类型构造返回消息
     * @Params [map] 封装了解析结果的Map
     * @Return java.lang.String 响应消息
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    public static String buildResponseMessage(Map map) {
        //响应消息
        String responseMessage = "";
        //得到消息类型
        String msgType = map.get("MsgType").toString();
        System.out.println("MsgType:" + msgType);
        //消息类型
        MessageType messageEnumType = MessageType.valueOf(MessageType.class, msgType.toUpperCase());
        switch (messageEnumType) {
            case TEXT:
                //处理文本消息
                responseMessage = handleTextMessage(map);
                break;
            case IMAGE:
                //处理图片消息
                responseMessage = handleImageMessage(map);
                break;
            case VOICE:
                //处理语音消息
                responseMessage = handleVoiceMessage(map);
                break;
            case VIDEO:
                //处理视频消息
                responseMessage = handleVideoMessage(map);
                break;
            case SHORTVIDEO:
                //处理小视频消息
                responseMessage = handleSmallVideoMessage(map);
                break;
            case LOCATION:
                //处理位置消息
                responseMessage = handleLocationMessage(map);
                break;
            case LINK:
                //处理链接消息
                responseMessage = handleLinkMessage(map);
                break;
            case EVENT:
                //处理事件消息,用户在关注与取消关注公众号时，微信会向我们的公众号服务器发送事件消息,开发者接收到事件消息后就可以给用户下发欢迎消息
                responseMessage = handleEventMessage(map);
            default:
                break;
        }
        //返回响应消息
        return responseMessage;
    }

    /**
     * @Description 接收到文本消息后根据其内容进行处理
     * @Params [map] 封装了解析结果的Map
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String handleTextMessage(Map<String, String> map) {

        //响应消息
        String responseMessage;
        // 消息内容
        String content = map.get("Content");
        switch (content) {
            case "文本":
                String msgText = "这是一条文本";
                responseMessage = buildTextMessage(map, msgText);
                break;
            case "图片":
                //通过素材管理接口上传图片时得到的media_id
                String imgMediaId = "";
                responseMessage = buildImageMessage(map, imgMediaId);
                break;
            case "语音":
                //通过素材管理接口上传语音文件时得到的media_id
                String voiceMediaId = "";
                responseMessage = buildVoiceMessage(map, voiceMediaId);
                break;
            case "图文":
                responseMessage = buildNewsMessage(map);
                break;
            case "音乐":
                Music music = new Music();
                music.title = "标题";
                music.description = "描述";
                music.musicUrl = "";
                music.hqMusicUrl = "";
                responseMessage = buildMusicMessage(map, music);
                break;
            case "视频":
                Video video = new Video();
                video.mediaId = "";
                video.title = "标题";
                video.description = "描述";
                responseMessage = buildVideoMessage(map, video);
                break;
            default:
                responseMessage = buildWelcomeTextMessage(map);
                break;

        }
        //返回响应消息
        return responseMessage;
    }


    /**
     * @Description 生成消息创建时间 （整型）
     * @Params []
     * @Return java.lang.String 消息创建时间
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String getMessageCreateTime() {
        Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
        String nowTime = df.format(dt);
        long dd = (long) 0;
        try {
            dd = df.parse(nowTime).getTime();
        } catch (Exception e) {

        }
        return String.valueOf(dd);
    }

    /**
     * @Description 构建默认返回消息
     * @Params [map] 封装了解析结果的Map
     * @Return java.lang.String responseMessageXml
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String buildWelcomeTextMessage(Map<String, String> map) {
        String responseMessageXml;
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        responseMessageXml = String
                .format(
                        "<xml>" +
                                "<ToUserName><![CDATA[%s]]></ToUserName>" +
                                "<FromUserName><![CDATA[%s]]></FromUserName>" +
                                "<CreateTime>%s</CreateTime>" +
                                "<MsgType><![CDATA[text]]></MsgType>" +
                                "<Content><![CDATA[%s]]></Content>" +
                                "</xml>",
                        fromUserName, toUserName, getMessageCreateTime(),
                        "感谢您使用证件识别助手，点击菜单里的按钮即可使用公众号提供的服务！");
        return responseMessageXml;
    }


    /**
     * @Description 构造文本消息
     * @Params [map 封装了解析结果的Map, content 文本消息内容]
     * @Return java.lang.String 文本消息XML字符串
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String buildTextMessage(Map<String, String> map, String content) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 文本消息XML数据格式
         * <xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>1348831860</CreateTime>
         <MsgType><![CDATA[text]]></MsgType>
         <Content><![CDATA[this is a test]]></Content>
         <MsgId>1234567890123456</MsgId>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[text]]></MsgType>" +
                        "<Content><![CDATA[%s]]></Content>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), content);
    }

    /**
     * @Description 构造图片消息
     * @Params [map 封装了解析结果的Map, mediaId 通过素材管理接口上传多媒体文件得到的id]
     * @Return java.lang.String 图片消息XML字符串
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String buildImageMessage(Map<String, String> map, String mediaId) {

        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 图片消息XML数据格式
         *<xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>12345678</CreateTime>
         <MsgType><![CDATA[image]]></MsgType>
         <Image>
         <MediaId><![CDATA[media_id]]></MediaId>
         </Image>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[image]]></MsgType>" +
                        "<Image>" +
                        "   <MediaId><![CDATA[%s]]></MediaId>" +
                        "</Image>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), mediaId);
    }

    /**
     * @Description 构造音乐消息
     * @Params [map 封装了解析结果的Map, music 封装好的音乐消息内容]
     * @Return java.lang.String 音乐消息XML字符串
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String buildMusicMessage(Map<String, String> map, Music music) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 音乐消息XML数据格式
         *<xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>12345678</CreateTime>
         <MsgType><![CDATA[music]]></MsgType>
         <Music>
         <Title><![CDATA[TITLE]]></Title>
         <Description><![CDATA[DESCRIPTION]]></Description>
         <MusicUrl><![CDATA[MUSIC_Url]]></MusicUrl>
         <HQMusicUrl><![CDATA[HQ_MUSIC_Url]]></HQMusicUrl>
         <ThumbMediaId><![CDATA[media_id]]></ThumbMediaId>
         </Music>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[music]]></MsgType>" +
                        "<Music>" +
                        "   <Title><![CDATA[%s]]></Title>" +
                        "   <Description><![CDATA[%s]]></Description>" +
                        "   <MusicUrl><![CDATA[%s]]></MusicUrl>" +
                        "   <HQMusicUrl><![CDATA[%s]]></HQMusicUrl>" +
                        "</Music>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), music.title, music.description, music.musicUrl, music.hqMusicUrl);
    }

    /**
     * @Description 构造视频消息
     * @Params [map, video] 封装了解析结果的Map 封装好的视频消息内容
     * @Return java.lang.String 视频消息XML字符串
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String buildVideoMessage(Map<String, String> map, Video video) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 音乐消息XML数据格式
         *<xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>12345678</CreateTime>
         <MsgType><![CDATA[video]]></MsgType>
         <Video>
         <MediaId><![CDATA[media_id]]></MediaId>
         <Title><![CDATA[title]]></Title>
         <Description><![CDATA[description]]></Description>
         </Video>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[video]]></MsgType>" +
                        "<Video>" +
                        "   <MediaId><![CDATA[%s]]></MediaId>" +
                        "   <Title><![CDATA[%s]]></Title>" +
                        "   <Description><![CDATA[%s]]></Description>" +
                        "</Video>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), video.mediaId, video.title, video.description);
    }

    /**
     * @Description 构造语音消息
     * @Params [map, mediaId] 封装了解析结果的Map 通过素材管理接口上传多媒体文件得到的id
     * @Return java.lang.String 语音消息XML字符串
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String buildVoiceMessage(Map<String, String> map, String mediaId) {
        //发送方帐号
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        /**
         * 语音消息XML数据格式
         *<xml>
         <ToUserName><![CDATA[toUser]]></ToUserName>
         <FromUserName><![CDATA[fromUser]]></FromUserName>
         <CreateTime>12345678</CreateTime>
         <MsgType><![CDATA[voice]]></MsgType>
         <Voice>
         <MediaId><![CDATA[media_id]]></MediaId>
         </Voice>
         </xml>
         */
        return String.format(
                "<xml>" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>" +
                        "<CreateTime>%s</CreateTime>" +
                        "<MsgType><![CDATA[voice]]></MsgType>" +
                        "<Voice>" +
                        "   <MediaId><![CDATA[%s]]></MediaId>" +
                        "</Voice>" +
                        "</xml>",
                fromUserName, toUserName, getMessageCreateTime(), mediaId);
    }


    /**
     * @Description 构造图文消息
     * @Params [map] 封装了解析结果的Map
     * @Return java.lang.String 图文消息XML字符串
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String buildNewsMessage(Map<String, String> map) {
        String fromUserName = map.get("FromUserName");
        // 开发者微信号
        String toUserName = map.get("ToUserName");
        NewsItem item = new NewsItem();
        item.Title = "标题";
        item.Description = "：\n" +
                "\n" +
                "　\n" +
                "\n" +
                "";
        item.PicUrl = "";
        item.Url = "";
        String itemContent1 = buildSingleItem(item);

        NewsItem item2 = new NewsItem();
        item2.Title = "标题";
        item2.Description = "描述";
        item2.PicUrl = "";
        item2.Url = "";
        String itemContent2 = buildSingleItem(item2);


        String content = String.format("<xml>\n" +
                "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                "<CreateTime>%s</CreateTime>\n" +
                "<MsgType><![CDATA[news]]></MsgType>\n" +
                "<ArticleCount>%s</ArticleCount>\n" +
                "<Articles>\n" + "%s" +
                "</Articles>\n" +
                "</xml> ", fromUserName, toUserName, getMessageCreateTime(), 2, itemContent1 + itemContent2);
        return content;

    }

    /**
     * @Description 生成图文消息的一条记录
     * @Params [item]
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String buildSingleItem(NewsItem item) {
        String itemContent = String.format("<item>\n" +
                "<Title><![CDATA[%s]]></Title> \n" +
                "<Description><![CDATA[%s]]></Description>\n" +
                "<PicUrl><![CDATA[%s]]></PicUrl>\n" +
                "<Url><![CDATA[%s]]></Url>\n" +
                "</item>", item.Title, item.Description, item.PicUrl, item.Url);
        return itemContent;
    }

    /**
     * @Description 处理接收到图片消息
     * @Params [map]
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String handleImageMessage(Map<String, String> map) {
        String picUrl = map.get("PicUrl");
        String mediaId = map.get("MediaId");
        System.out.print("picUrl:" + picUrl);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的图片，图片Url为：%s\n图片素材Id为：%s", picUrl, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * @Description 处理接收到语音消息
     * @Params [map]
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String handleVoiceMessage(Map<String, String> map) {
        String format = map.get("Format");
        String mediaId = map.get("MediaId");
        System.out.print("format:" + format);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的语音，语音格式为：%s\n语音素材Id为：%s", format, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * @Description 处理接收到的视频消息
     * @Params [map]
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String handleVideoMessage(Map<String, String> map) {
        String thumbMediaId = map.get("ThumbMediaId");
        String mediaId = map.get("MediaId");
        System.out.print("thumbMediaId:" + thumbMediaId);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的视频，视频中的素材ID为：%s\n视频Id为：%s", thumbMediaId, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * @Description 处理接收到的小视频消息
     * @Params [map]
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String handleSmallVideoMessage(Map<String, String> map) {
        String thumbMediaId = map.get("ThumbMediaId");
        String mediaId = map.get("MediaId");
        System.out.print("thumbMediaId:" + thumbMediaId);
        System.out.print("mediaId:" + mediaId);
        String result = String.format("已收到您发来的小视频，小视频中素材ID为：%s,\n小视频Id为：%s", thumbMediaId, mediaId);
        return buildTextMessage(map, result);
    }

    /**
     * @Description 处理接收到的地理位置消息
     * @Params [map]
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String handleLocationMessage(Map<String, String> map) {
        String latitude = map.get("Location_X");  //纬度
        String longitude = map.get("Location_Y");  //经度
        String label = map.get("Label");  //地理位置精度
        String result = String.format("纬度：%s\n经度：%s\n地理位置：%s", latitude, longitude, label);
        return buildTextMessage(map, result);
    }

    /**
     * @Description 处理接收到的链接消息
     * @Params [map]
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String handleLinkMessage(Map<String, String> map) {
        String title = map.get("Title");
        String description = map.get("Description");
        String url = map.get("Url");
        String result = String.format("已收到您发来的链接，链接标题为：%s,\n描述为：%s\n,链接地址为：%s", title, description, url);
        return buildTextMessage(map, result);
    }

    /**
     * @Description 处理消息Message
     * @Params [map] 封装了解析结果的Map
     * @Return java.lang.String
     * @Author aria
     * @Date 18-11-26
     * @Throw
     * @Other
     */
    private static String handleEventMessage(Map<String, String> map) {
        String responseMessage = buildWelcomeTextMessage(map);
        return responseMessage;
    }

}

/**
 * 图文消息
 */
class NewsItem {
    public String Title;

    public String Description;

    public String PicUrl;

    public String Url;
}

/**
 * 音乐消息
 */
class Music {
    public String title;
    public String description;
    public String musicUrl;
    public String hqMusicUrl;
}

/**
 * 视频消息
 */
class Video {
    public String title;
    public String description;
    public String mediaId;
}
