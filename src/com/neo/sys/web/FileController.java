package com.neo.sys.web;

import java.io.File;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.neo.sys.entity.bean.SysFile;
import com.neo.sys.service.service.SysFileService;
import com.neo.sys.utils.FileUtils;

public class FileController extends BaseController{

	@Autowired
	protected SysFileService sysFileService;
	
	@Autowired  
	private  HttpServletRequest request;  
	
	@RequestMapping("download")
	public ResponseEntity<byte[]> download(String id) throws Exception {
		SysFile sysFile = sysFileService.getFileDetail(id);
		if (sysFile == null) {
			throw new FileNotFoundException("文件飞了。。。。");
		}
		String path = sysFile.getLocationFilePath();
		String type = sysFile.getFileType();
		String savePath = request.getServletContext().getRealPath(path);
		File file = new File(savePath);
		HttpHeaders headers = new HttpHeaders();
		String fileName = new String((sysFile.getFileName() +(type==null?"":("."+type))).getBytes("UTF-8"),
				"iso-8859-1");// 为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}
	
}
