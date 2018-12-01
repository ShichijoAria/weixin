package org.yorha.weixin.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yorha.weixin.util.APPCodeDemo;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ImageServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletInputStream inputStream = request.getInputStream();
		String info = APPCodeDemo.getInfo(inputStream, "face");
		PrintWriter printWriter=response.getWriter();
		printWriter.print(info);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		ServletInputStream inputStream = request.getInputStream();
		int b;
		StringBuilder stringBuilder=new StringBuilder();
		List<Integer> list=new ArrayList<>();
		StringBuilder str=new StringBuilder();
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		byte[]content=new byte[2];
		/*while((b=inputStream.read(content))!=-1) {
			stringBuilder.append(new String(content,0,b));
		}*/
		String line;
		 while ((line = reader.readLine()) != null) {   
			 stringBuilder.append(line);
			
         } 
		 
		PrintWriter printWriter=response.getWriter();
		printWriter.print(stringBuilder.toString());	
	/*	
		String info = APPCodeDemo.getInfo(inputStream, "face");
		PrintWriter printWriter=response.getWriter();
		printWriter.print(info);*/
	}

}