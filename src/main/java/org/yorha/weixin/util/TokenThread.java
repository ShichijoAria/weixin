package org.yorha.weixin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.yorha.weixin.entity.AccessToken;

import java.io.IOException;

public class TokenThread implements Runnable {



    public static AccessToken accessToken = null;

    public void run(){
        while (true){
            try{
                accessToken = WeChatUtil.getAccessToken();
                if(null!=accessToken){
                    System.out.println(accessToken.getAccessToken());
                    Thread.sleep(7000 * 1000); //获取到access_token 休眠7000秒

                }else{
                    Thread.sleep(1000*3); //获取的access_token为空 休眠3秒
                }
            }catch(Exception e){
                System.out.println("发生异常："+e.getMessage());
                e.printStackTrace();
                try{
                    Thread.sleep(1000*10); //发生异常休眠1秒
                }catch (Exception e1){

                }
            }
        }
    }

}