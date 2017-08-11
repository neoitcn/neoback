package com.qixin.neoback.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.qixin.neoback.biz.AdminBiz;
import com.qixin.neoback.entity.Edu_news;

@Controller
@RequestMapping(value = "admin")
@SessionAttributes("")
public class AdminController {
	@Resource(name = "adminBiz")
	private AdminBiz adminBiz;

	// 1.上传新闻
	@RequestMapping(value = "/uploadNews")
	public String uploadNews(Edu_news news, String[] pic, String content,
			@RequestParam(value = "smallpicture") MultipartFile smallpicture, Map<String, Object> map,
			HttpServletRequest request) {

		try {
			// -----------------1.上传新闻缩略图
			// 若标题图片为空则返回
			if (smallpicture.getOriginalFilename().equals("")) {
				request.setAttribute("msg", "请选择新闻标题图片..");
				return "main";
			}

			String filename = smallpicture.getOriginalFilename();// 获取缩略图名称
			int index = filename.lastIndexOf(".");
			String filetype = filename.substring(index + 1);// 获得图片后缀名
			System.out.println("图片后缀:" + filetype);
			System.out.println("图片名称:" + smallpicture.getOriginalFilename());
			System.out.println("图片smallpicture:" + smallpicture);
			System.out.println("上传图片的类型:" + smallpicture.getContentType());

			// 定义新闻缩略图上传类型
			List<String> typelist = new ArrayList<String>();
			typelist.add("JPG");
			typelist.add("jpg");
			typelist.add("GIF");
			typelist.add("gif");
			typelist.add("BMP");
			typelist.add("bmp");
			typelist.add("PNG");
			typelist.add("png");
			//图片类型不对则返回..
			if (!typelist.contains(filetype)) {
				request.setAttribute("msg", "上传文件类型不对..");
				return "main";
			}
			
			long lon = new Date().getTime();//利用当前日期作为 缩略图名称 (也用做下面新闻地址)
			System.out.println("lon1:" + lon);
			//设定图片保存的路径path
			String path = "E:/github/neoback/WebContent/news/images/titleimages/";
             // 保存文件(两种:1.通过流读取和写入,2.通过MultipartFile的transferTo保存)
			String picpath = path + lon + "." + filetype;
			smallpicture.transferTo(new File(picpath));
			System.out.println("图片上传成功.......");
			request.setAttribute("msg", "图片上传成功1..");
			// ---------------2.以下是新闻内容
			System.out.println("content:" + content);
			// 加上HTML头和尾 ,生成HTML页面;
			String htmlcontent;
			htmlcontent = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Insert title here</title></head><body>"
					+ content + "</body></html>";

			System.out.println("lon2:" + lon);
			File file = new File("E:/github/neoback/WebContent/news/" + news.getType() + "/" + lon + ".html");
			if (!file.exists()) {
				file.createNewFile();
				if (!file.createNewFile()) {
					System.out.println("news下面创建了新文件...");
				}
			}
			// 通过字符流写入到指定文件夹
			FileOutputStream out = new FileOutputStream(file, true);
			StringBuffer sb = new StringBuffer();
			sb.append(htmlcontent);
			out.write(sb.toString().getBytes("utf-8"));
			out.close();
			map.put("content", htmlcontent);
			
			//----------3.补充news的一些前台没传过来的数据  
			news.setHtmlUrl(lon+".html");
			news.setTitleImage(lon+"."+filetype);
			news.setCreateTime(new Date());
			news.setEnabled(0);
			news.setDeleted(0);
			 //添加新闻
			adminBiz.insertSelective(news);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Done");

		return "main";
	}

}
