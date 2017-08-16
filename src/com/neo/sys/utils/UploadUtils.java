package com.neo.sys.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class UploadUtils {

	public static List<MultipartFile> getUpFiles(HttpServletRequest request) throws Exception {
		List<MultipartFile> listFiles = new ArrayList<>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					listFiles.add(file);
				}
			}
		}
		return listFiles;
	}
	/**
	 * 同时获取文件和表单数据
	 * @param request
	 * @param listParam //要获取的表单数据
	 * @return 获取文件列表，需要使用_listFiles_，请不要在其他字段上使用该KEY，否则会被覆盖。
	 * @throws Exception
	 */
	public static Map<String,Object> getUpFiles(HttpServletRequest request,List<String> listParam) throws Exception {
		List<MultipartFile> listFiles = new ArrayList<>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					listFiles.add(file);
				}
			}
		}
		Map<String,Object> map = new HashMap<>();
		if(listParam!= null){
			for(String str:listParam){
				map.put(str, request.getParameter(str));
			}
		}
		map.put("_listFiles_", listFiles);
		return map;
	}

}
