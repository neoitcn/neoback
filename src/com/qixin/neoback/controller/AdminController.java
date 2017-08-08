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

	// �ļ��ϴ���Ҫ�Ĳ���
	/*
	 * private File upload;// �ϴ����ļ� private String uploadFileName;//�ϴ����ļ�����
	 * private String uploadContentType;// �����ļ��ϴ���MIME����
	 */
	@RequestMapping(value = "/uploadNews")
	public String uploadNews( String content,HttpServletRequest request){
		
		Map map=new HashMap<>();
		
		System.out.println("������:"+content);
		map.put("content", content);
		
		return "main";
		
	}
	
	
	
	@RequestMapping(value = "/addNews", method = RequestMethod.POST)
	public String addNews(@RequestParam(value = "headimg") MultipartFile headimg, News news, String newscontent,
			String picturedesc, HttpServletRequest req) {

		try {

			System.out.println("����addNews...��������:" + newscontent);
			System.out.println("ͼƬ����:" + headimg.getOriginalFilename());
			System.out.println("ͼƬdesc:" + picturedesc);
			System.out.println("�ϴ�ͼƬ������:" + headimg.getContentType());

			System.out.println("����:" + news.getAuthor());
			System.out.println("���ŵ�ַ:" + news.getHtmlUrl());
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
			// �ϴ�����ͼƬ
			String picname = null;

			if (headimg != null) {
				// �趨ͼƬ�����·��path
				 /*File file=new File("neoback/WebContent/NEWS/IMAGES");
				 
				 //"C:/Users/zhu/workspace/neoback/WebContent/NEWS/IMAGES/";
				 String path=file.getAbsolutePath().replace("\\", "/")+"/";
			     System.out.println("gg:"+path);*/
			    String path=" C:/Users/zhu/workspace/neoback/WebContent/NEWS/IMAGES/";
			    
			    
			    
			    
			    
			    
			    
			    File f2 = new File(this.getClass().getResource("").getPath());  
		        System.out.println("dd:"+f2); 
		        File directory = new File("");// ����Ϊ��  
		        String courseFile = directory.getCanonicalPath();  
		        System.out.println("KK:"+courseFile);  
				
				
				String filename = headimg.getOriginalFilename();// ��ȡ�ļ�����
				int index = filename.lastIndexOf(".");
				String filetype = filename.substring(index + 1);
				System.out.println("4..." + filetype);
				if (!typelist.contains(filetype)) {

					req.setAttribute("msg", "�ϴ��ļ����Ͳ���..");
					return "main";
				}

				// filepath=req.getServletContext().getRealPath("/images/"+date.getTime()+filename);
				// �����ļ�(����:1.ͨ������ȡ��д��,2.ͨ��MultipartFile��transferTo����)
				picname = new Date().getTime() + "_" + filename;

				String picpath = path + picname;
				headimg.transferTo(new File(picpath));

				req.setAttribute("msg", "ͼƬ�ϴ��ɹ�..");
			}

			// ������ű�
			Date aa = new Date();

			news.setCreateTime(aa);
			adminBiz.insert(news);
			// ------------------------------ ������ �������������ļ�HTML-----------------
			Configuration cfg = new Configuration();
			// ����ģ��·��test.ftl
			 //File file = new File("WebContent/NEWS/newsmold"); String dir = file.getAbsolutePath().replace("\\", "/") + "/";
			 String dir ="C:/Users/zhu/workspace/neoback/WebContent/NEWS/newsmold/";
			 

			// ���������ģ���ļ�
			cfg.setDirectoryForTemplateLoading(new File(dir));
			// ���ö����װ��
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			// �����쳣������
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

			// ��������ģ�� (���͵�����չʾҳ�������)
			Map map = new HashMap();
			map.put("abc", "��о������ӭ��....");
			map.put("news", news);
			map.put("newscontent", newscontent);
			map.put("picname", picname);

			// ͨ��freemarker����ģ�壬������Ҫ���Template����
			Template template = cfg.getTemplate("test.ftl");
			// ����ģ��������֮������
			String html = "C:/Users/zhu/workspace/neoback/WebContent/NEWS/" + news.getLevel() + "/";
			PrintWriter out = new PrintWriter(
					new BufferedWriter(new FileWriter(html + "/" + new Date().getTime() + ".html")));

			// ����ģ��
			template.process(map, out);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "NEWS/newsshow";

	}

}
