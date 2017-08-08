package com.qixin.neoback.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.qixin.neoback.biz.AdminBiz;
import com.qixin.neoback.entity.News;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@Controller
@RequestMapping(value = "admin")
@SessionAttributes("")
public class AdminController {
	@Resource(name = "adminBiz")
	private AdminBiz adminBiz;

	// 文件上传需要的参数
	/*
	 * private File upload;// 上传的文件 private String uploadFileName;//上传的文件名陈
	 * private String uploadContentType;// 接收文件上传的MIME类型
	 */
	@RequestMapping(value = "/uploadNews")
	public String uploadNews( String content,HttpServletRequest request){
		
		Map map=new HashMap<>();
		
		System.out.println("内容是:"+content);
		map.put("content", content);
		
		return "main";
		
	}
	
	
	
	@RequestMapping(value = "/addNews", method = RequestMethod.POST)
	public String addNews(@RequestParam(value = "headimg") MultipartFile headimg, News news, String newscontent,
			String picturedesc, HttpServletRequest req) {

		try {

			System.out.println("进入addNews...新闻内容:" + newscontent);
			System.out.println("图片名称:" + headimg.getOriginalFilename());
			System.out.println("图片desc:" + picturedesc);
			System.out.println("上传图片的类型:" + headimg.getContentType());

			System.out.println("作者:" + news.getAuthor());
			System.out.println("新闻地址:" + news.getHtmlUrl());
			List<String> typelist = new ArrayList<String>();
			typelist.add("JPG");
			typelist.add("jpg");
			typelist.add("GIF");
			typelist.add("gif");
			typelist.add("BMP");
			typelist.add("bmp");
			typelist.add("PNG");
			typelist.add("png");

			System.out.println("headimg:" + headimg.getOriginalFilename());
			// 上传新闻图片
			String picname = null;

			if (headimg != null) {
				// 设定图片保存的路径path
				 /*File file=new File("neoback/WebContent/NEWS/IMAGES");
				 
				 //"C:/Users/zhu/workspace/neoback/WebContent/NEWS/IMAGES/";
				 String path=file.getAbsolutePath().replace("\\", "/")+"/";
			     System.out.println("gg:"+path);*/
			    String path=" C:/Users/zhu/workspace/neoback/WebContent/NEWS/IMAGES/";
			    
			    
			    
			    
			    
			    
			    
			    File f2 = new File(this.getClass().getResource("").getPath());  
		        System.out.println("dd:"+f2); 
		        File directory = new File("");// 参数为空  
		        String courseFile = directory.getCanonicalPath();  
		        System.out.println("KK:"+courseFile);  
				
				
				String filename = headimg.getOriginalFilename();// 获取文件名称
				int index = filename.lastIndexOf(".");
				String filetype = filename.substring(index + 1);
				System.out.println("4..." + filetype);
				if (!typelist.contains(filetype)) {

					req.setAttribute("msg", "上传文件类型不对..");
					return "main";
				}

				// filepath=req.getServletContext().getRealPath("/images/"+date.getTime()+filename);
				// 保存文件(两种:1.通过流读取和写入,2.通过MultipartFile的transferTo保存)
				picname = new Date().getTime() + "_" + filename;

				String picpath = path + picname;
				headimg.transferTo(new File(picpath));

				req.setAttribute("msg", "图片上传成功..");
			}

			// 添加新闻表
			Date aa = new Date();

			news.setCreateTime(aa);
			adminBiz.insert(news);
			// ------------------------------ 以下是 生成新闻链接文件HTML-----------------
			Configuration cfg = new Configuration();
			// 设置模板路径test.ftl
			 //File file = new File("WebContent/NEWS/newsmold"); String dir = file.getAbsolutePath().replace("\\", "/") + "/";
			 String dir ="C:/Users/zhu/workspace/neoback/WebContent/NEWS/newsmold/";
			 

			// 从哪里加载模板文件
			cfg.setDirectoryForTemplateLoading(new File(dir));
			// 设置对象包装器
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			// 设置异常处理器
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

			// 定义数据模型 (传送到新闻展示页面的数据)
			Map map = new HashMap();
			map.put("abc", "启芯教育欢迎你....");
			map.put("news", news);
			map.put("newscontent", newscontent);
			map.put("picname", picname);

			// 通过freemarker解释模板，首先需要获得Template对象
			Template template = cfg.getTemplate("test.ftl");
			// 定义模板解释完成之后的输出
			String html = "C:/Users/zhu/workspace/neoback/WebContent/NEWS/" + news.getLevel() + "/";
			PrintWriter out = new PrintWriter(
					new BufferedWriter(new FileWriter(html + "/" + new Date().getTime() + ".html")));

			// 解释模板
			template.process(map, out);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "NEWS/newsshow";

	}

}
