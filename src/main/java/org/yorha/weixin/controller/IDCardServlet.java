package org.yorha.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.yorha.weixin.util.HttpUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author aria
 * @Date 18-12-1
 * @Project_name weixin
 */
@WebServlet(name = "IDCard",
        urlPatterns = "/IDCard"
)
public class IDCardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/IDCard.html").forward(req,resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        String host = "http://dm-51.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_idcard.json";
        String appcode = "9293e188cd58422ab6a72856f227810f";
        Boolean is_old_format = false;//如果文档的输入中含有inputs字段，设置为True， 否则设置为False
        //请根据线上文档修改configure字段
        JSONObject configObj = new JSONObject();
        configObj.put("side", "face");
        String config_str = configObj.toString();
        //            configObj.put("min_size", 5);
        //            String config_str = "";

        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        String picCode= JSON.parseObject(HttpUtils.readJSONString(request)).getString("base64");
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "{\"image\":\""+picCode.substring(23)+"\","+"\"configure\": \"{\\\"side\\\":\\\"face\\\"}\""+"}";
        // 对图像进行base64编码


        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            if(is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
                System.out.println(out.toJSONString());
            }else{
                resp.getWriter().println("名字："+res_obj.getString("name")+',');
                resp.getWriter().println("地址："+res_obj.getString("address")+',');
                resp.getWriter().println("身份证号码："+res_obj.getString("num")+',');
                resp.getWriter().println("民族："+res_obj.getString("nationality")+',');
                resp.getWriter().println("生日："+res_obj.getString("birth")+',');
                resp.getWriter().println("性别："+res_obj.getString("sex")+',');



                System.out.println(res_obj.toJSONString());
            }
        } catch (Exception e) {
            resp.getWriter().print("fail");
            e.printStackTrace();
        }
    }
}
