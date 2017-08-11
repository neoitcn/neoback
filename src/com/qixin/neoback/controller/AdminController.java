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

	// 1.�ϴ�����
	@RequestMapping(value = "/uploadNews")
	public String uploadNews(Edu_news news, String[] pic, String content,
			@RequestParam(value = "smallpicture") MultipartFile smallpicture, Map<String, Object> map,
			HttpServletRequest request) {

		try {
			// -----------------1.�ϴ���������ͼ
			// ������ͼƬΪ���򷵻�
			if (smallpicture.getOriginalFilename().equals("")) {
				request.setAttribute("msg", "��ѡ�����ű���ͼƬ..");
				return "main";
			}

			String filename = smallpicture.getOriginalFilename();// ��ȡ����ͼ����
			int index = filename.lastIndexOf(".");
			String filetype = filename.substring(index + 1);// ���ͼƬ��׺��
			System.out.println("ͼƬ��׺:" + filetype);
			System.out.println("ͼƬ����:" + smallpicture.getOriginalFilename());
			System.out.println("ͼƬsmallpicture:" + smallpicture);
			System.out.println("�ϴ�ͼƬ������:" + smallpicture.getContentType());

			// ������������ͼ�ϴ�����
			List<String> typelist = new ArrayList<String>();
			typelist.add("JPG");
			typelist.add("jpg");
			typelist.add("GIF");
			typelist.add("gif");
			typelist.add("BMP");
			typelist.add("bmp");
			typelist.add("PNG");
			typelist.add("png");
			//ͼƬ���Ͳ����򷵻�..
			if (!typelist.contains(filetype)) {
				request.setAttribute("msg", "�ϴ��ļ����Ͳ���..");
				return "main";
			}
			
			long lon = new Date().getTime();//���õ�ǰ������Ϊ ����ͼ���� (Ҳ�����������ŵ�ַ)
			System.out.println("lon1:" + lon);
			//�趨ͼƬ�����·��path
			String path = "E:/github/neoback/WebContent/news/images/titleimages/";
             // �����ļ�(����:1.ͨ������ȡ��д��,2.ͨ��MultipartFile��transferTo����)
			String picpath = path + lon + "." + filetype;
			smallpicture.transferTo(new File(picpath));
			System.out.println("ͼƬ�ϴ��ɹ�.......");
			request.setAttribute("msg", "ͼƬ�ϴ��ɹ�1..");
			// ---------------2.��������������
			System.out.println("content:" + content);
			// ����HTMLͷ��β ,����HTMLҳ��;
			String htmlcontent;
			htmlcontent = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Insert title here</title></head><body>"
					+ content + "</body></html>";

			System.out.println("lon2:" + lon);
			File file = new File("E:/github/neoback/WebContent/news/" + news.getType() + "/" + lon + ".html");
			if (!file.exists()) {
				file.createNewFile();
				if (!file.createNewFile()) {
					System.out.println("news���洴�������ļ�...");
				}
			}
			// ͨ���ַ���д�뵽ָ���ļ���
			FileOutputStream out = new FileOutputStream(file, true);
			StringBuffer sb = new StringBuffer();
			sb.append(htmlcontent);
			out.write(sb.toString().getBytes("utf-8"));
			out.close();
			map.put("content", htmlcontent);
			
			//----------3.����news��һЩǰ̨û������������  
			news.setHtmlUrl(lon+".html");
			news.setTitleImage(lon+"."+filetype);
			news.setCreateTime(new Date());
			news.setEnabled(0);
			news.setDeleted(0);
			 //�������
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
