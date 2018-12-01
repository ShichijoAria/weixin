package org.yorha.weixin.controller;

import com.alibaba.fastjson.JSON;
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
 * @Date 18-11-29
 * @Project_name weixin
 */
@WebServlet(name = "creditCard",
        urlPatterns = "/creditCard"
)
public class CreditCardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/index.html").forward(req,resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String host = "https://yhk.market.alicloudapi.com";
        String path = "/rest/160601/ocr/ocr_bank_card.json";
        String method = "POST";
        String appcode = "7abe6fd5ec4343cc9573c373f8baecb2";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为A uthorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        //获取jsp传过来的base64编码
        String picCode= JSON.parseObject(HttpUtils.readJSONString(request)).getString("base64");
        String bodys = "{\"image\":\""+picCode.substring(23)+"\"}";

        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            HttpResponse thisresponse = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            JSONObject result= JSON.parseObject(EntityUtils.toString(thisresponse.getEntity()));
            if(result.getString("success").equals("true")) {
                response.getWriter().println(result.getString("card_num"));
            }else
                response.getWriter().println("识别失败");
        } catch (Exception e) {
            response.getWriter().println("识别失败");
            e.printStackTrace();
        }
    }

}
