package org.yorha.weixin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.binary.Base64.encodeBase64;



/**
 * 浣跨敤APPCODE杩涜浜戝競鍦簅cr鏈嶅姟鎺ュ彛璋冪敤
 */

public class APPCodeDemo {

    /*
     * 鑾峰彇鍙傛暟鐨刯son瀵硅薄
     */
    public static JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void main(String[] args){

        String host = "https://dm-52.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_driver_license.json";
        String appcode = "浣犵殑APPCODE";
        String imgFile = "鍥剧墖璺緞";
        Boolean is_old_format = false;//濡傛灉鏂囨。鐨勮緭鍏ヤ腑鍚湁inputs瀛楁锛岃缃负True锛� 鍚﹀垯璁剧疆涓篎alse
        //璇锋牴鎹嚎涓婃枃妗ｄ慨鏀筩onfigure瀛楁
        JSONObject configObj = new JSONObject();
        configObj.put("side", "face");
        String config_str = configObj.toString();
        //            configObj.put("min_size", 5);
        //            String config_str = "";

        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //鏈�鍚庡湪header涓殑鏍煎紡(涓棿鏄嫳鏂囩┖鏍�)涓篈uthorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();

        // 瀵瑰浘鍍忚繘琛宐ase64缂栫爜
        String imgBase64 = "";
        try {
            File file = new File(imgFile);
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(encodeBase64(content));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // 鎷艰璇锋眰body鐨刯son瀛楃涓�
        JSONObject requestObj = new JSONObject();
        try {
            if(is_old_format) {
                JSONObject obj = new JSONObject();
                obj.put("image", getParam(50, imgBase64));
                if(config_str.length() > 0) {
                    obj.put("configure", getParam(50, config_str));
                }
                JSONArray inputArray = new JSONArray();
                inputArray.add(obj);
                requestObj.put("inputs", inputArray);
            }else{
                requestObj.put("image", imgBase64);
                if(config_str.length() > 0) {
                    requestObj.put("configure", config_str);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodys = requestObj.toString();

        try {
            /**
             * 閲嶈鎻愮ず濡備笅:
             * HttpUtils璇蜂粠
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 涓嬭浇
             *
             * 鐩稿簲鐨勪緷璧栬鍙傜収
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if(stat != 200){
                System.out.println("Http code: " + stat);
                System.out.println("http header error msg: "+ response.getFirstHeader("X-Ca-Error-Message"));
                System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                return;
            }

            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            if(is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
                System.out.println(out.toJSONString());
            }else{
                System.out.println(res_obj.toJSONString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String  getInfo(InputStream input,String side){

        String host = "https://dm-52.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_driver_license.json";
        String appcode = "8f6f0d8752c743dca3d744a791446837";
       /* String imgFile = absoPath;*/
        Boolean is_old_format = false;//如果文档的输入中含有inputs字段，设置为True， 否则设置为False
        //请根据线上文档修改configure字段
        JSONObject configObj = new JSONObject();
        /*configObj.put("side", "face");*/
        configObj.put("side", side);
        String config_str = configObj.toString();
        //            configObj.put("min_size", 5);
        //            String config_str = "";

        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();

        // 对图像进行base64编码
        String imgBase64 = "";
        int available=1;
		try {
			available = input.available();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        byte[] content = new byte[available];
        imgBase64 = new String(encodeBase64(content));
        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        try {
            if(is_old_format) {
                JSONObject obj = new JSONObject();
                obj.put("image", getParam(50, imgBase64));
                if(config_str.length() > 0) {
                    obj.put("configure", getParam(50, config_str));
                }
                JSONArray inputArray = new JSONArray();
                inputArray.add(obj);
                requestObj.put("inputs", inputArray);
            }else{
                requestObj.put("image", imgBase64);
                if(config_str.length() > 0) {
                    requestObj.put("configure", config_str);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodys = requestObj.toString();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if(stat != 200){
                System.out.println("Http code: " + stat);
                System.out.println("http header error msg: "+ response.getFirstHeader("X-Ca-Error-Message"));
                System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                return null;
            }

            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            if(is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
                return output.toString();
            }else{
            	return res_obj.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String  getInfo(String absoPath,String side){
    	 String host = "https://dm-52.data.aliyun.com";
         String path = "/rest/160601/ocr/ocr_driver_license.json";
         String appcode = "8f6f0d8752c743dca3d744a791446837";
         String imgFile =absoPath ;
         Boolean is_old_format = false;//濡傛灉鏂囨。鐨勮緭鍏ヤ腑鍚湁inputs瀛楁锛岃缃负True锛� 鍚﹀垯璁剧疆涓篎alse
         //璇锋牴鎹嚎涓婃枃妗ｄ慨鏀筩onfigure瀛楁
         JSONObject configObj = new JSONObject();
         configObj.put("side", side);
         String config_str = configObj.toString();
         //            configObj.put("min_size", 5);
         //            String config_str = "";

         String method = "POST";
         Map<String, String> headers = new HashMap<String, String>();
         //鏈�鍚庡湪header涓殑鏍煎紡(涓棿鏄嫳鏂囩┖鏍�)涓篈uthorization:APPCODE 83359fd73fe94948385f570e3c139105
         headers.put("Authorization", "APPCODE " + appcode);

         Map<String, String> querys = new HashMap<String, String>();

         // 瀵瑰浘鍍忚繘琛宐ase64缂栫爜
         String imgBase64 = "";
         try {
             File file = new File(imgFile);
             byte[] content = new byte[(int) file.length()];
             FileInputStream finputstream = new FileInputStream(file);
             finputstream.read(content);
             finputstream.close();
             imgBase64 = new String(encodeBase64(content));
         } catch (IOException e) {
             e.printStackTrace();
             return null;
         }
         // 鎷艰璇锋眰body鐨刯son瀛楃涓�
         JSONObject requestObj = new JSONObject();
         try {
             if(is_old_format) {
                 JSONObject obj = new JSONObject();
                 obj.put("image", getParam(50, imgBase64));
                 if(config_str.length() > 0) {
                     obj.put("configure", getParam(50, config_str));
                 }
                 JSONArray inputArray = new JSONArray();
                 inputArray.add(obj);
                 requestObj.put("inputs", inputArray);
             }else{
                 requestObj.put("image", imgBase64);
                 if(config_str.length() > 0) {
                     requestObj.put("configure", config_str);
                 }
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
         String bodys = requestObj.toString();

         try {
             /**
              * 閲嶈鎻愮ず濡備笅:
              * HttpUtils璇蜂粠
              * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
              * 涓嬭浇
              *
              * 鐩稿簲鐨勪緷璧栬鍙傜収
              * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
              */
             HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
             int stat = response.getStatusLine().getStatusCode();
             if(stat != 200){
                 System.out.println("Http code: " + stat);
                 System.out.println("http header error msg: "+ response.getFirstHeader("X-Ca-Error-Message"));
                 System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                 return null;
             }

             String res = EntityUtils.toString(response.getEntity());
             JSONObject res_obj = JSON.parseObject(res);
             if(is_old_format) {
                 JSONArray outputArray = res_obj.getJSONArray("outputs");
                 String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                 JSONObject out = JSON.parseObject(output);
                
                 return out.toJSONString();
             }else{
                 return res_obj.toJSONString();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
    	return null;
    }
}