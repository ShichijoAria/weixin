package org.yorha.weixin.controller;

import org.yorha.weixin.util.ResourceUtil;
import org.yorha.weixin.util.TokenThread;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @Description 启动tomcat时就启动该线程，定时获取accesstoken
 * @Author       Aria
 * @Date   18-11-18
 * @Project_name weixin
 *
 */
public class AccessTokenServlet extends HttpServlet {

    /*
     * @Description: 启动获取token的线程
     * @Param: []
     * @return: void
     * @Author:  aria
     * @Date: 18-11-25
     */
    public void init() throws ServletException {
        new Thread(new TokenThread()).start(); //启动进程
    }

}