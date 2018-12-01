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
 * @Date 18-12-1
 * @Project_name weixin
 */
@WebServlet(name = "carNum",
        urlPatterns = "/carNum"
)
public class CarNumServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/carNum.html").forward(req,resp);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String host = "http://anpr.sinosecu.com.cn";
        String path = "/api/recogliu.do";
        String method = "POST";
        String appcode = "8ad6a64255774f66ae109c80d8133ac3";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为A uthorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        //获取jsp传过来的base64编码
        String picCode= JSON.parseObject(HttpUtils.readJSONString(request)).getString("base64");


        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("img", picCode.substring(23));


        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try {
            HttpResponse thisresponse = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            JSONObject result = JSON.parseObject(EntityUtils.toString(thisresponse.getEntity()));
            String cardinfo=result.getString("cardsinfo");
            response.getWriter().println(cardinfo.substring(35,42
            ));
        } catch (Exception e) {
            response.getWriter().println("识别失败");
            e.printStackTrace();
        }
    }



}
