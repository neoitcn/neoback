package com.neo.sys.service.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.neo.sys.utils.ConfigValues;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.neo.sys.dao.SysFileDao;
import com.neo.sys.entity.bean.SysFile;
import com.neo.sys.utils.FileUtils;
import com.neo.sys.utils.UploadUtils;

@Service
public class SysFileService{

	@Autowired
	private SysFileDao sysFileDao;

	@Transactional(readOnly = true)
	public List<SysFile> getFiles(Map<String, Object> param) {
		return sysFileDao.getFiles(param);
	}

	@Transactional(readOnly = true)
	public List<SysFile> getFileByObjectId(String objectId) {
		return sysFileDao.getFileByObjectId(objectId);
	}

	/**
	 * 
	 * @param request 当前请求的request
	 * @param folder 要将文件保存到的目标路径，目前指定的是当前工程下的某一个路径
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public List<SysFile> addFile(HttpServletRequest request,String folder,String objectId) throws Exception{
	
		try {
			return saveFile(request,folder/*,null*/,objectId);
		} catch (Exception e) {
			rollbackFile(request,folder,null);
			throw e;
		}
		
	}
	/*@Transactional(readOnly = false)
	public List<SysFile> addFile(HttpServletRequest request,String folder,String fileName,String objectId) throws Exception{
		
		try {
			return saveFile(request,folder,fileName,objectId);
		} catch (Exception e) {
			rollbackFile(request,folder,fileName);
			throw e;
		}
	}*/

	@Transactional(readOnly = false)
	public int deleteFile(String id,HttpServletRequest request) throws Exception{
		try {
			SysFile sysFile = getFileDetail(id);
			if (sysFile == null) {
				throw new Exception("此条记录不存在");
			}
			String path = sysFile.getFilePath();
			String realPath = request.getServletContext().getRealPath("/").replaceAll("\\\\","/");
			if(!realPath.endsWith("/")){
				realPath = realPath+"/";
			}
			File f = new File(realPath+path);
			f.delete();
			if (f.exists()) {
				throw new Exception("删除失败，请重试！");
			}
		} catch (Exception e) {
			throw e;
		}
		return sysFileDao.deleteFile(id);
	}

	@Transactional(readOnly = false)
	public int deleteFileByObjectId(String objectId,HttpServletRequest request) throws Exception{
		int index = 0;
		try {
			List<SysFile> listFile = getFileByObjectId(objectId);
			if (listFile == null) {
				throw new Exception("没有这些记录");
			}
			String realPath = request.getServletContext().getRealPath("/"+ConfigValues.getDiyFolderPath()).replaceAll("\\\\","/");
			if(!realPath.endsWith("/")){
				realPath = realPath+"/";
			}
			for(SysFile file:listFile){
				String path = file.getFilePath();
				File f = new File(realPath+path);
				f.delete();
				if (f.exists()) {
					throw new Exception("删除失败，请重试！");
				}
				index += sysFileDao.deleteFile(file.getId());
			}
		} catch (Exception e) {
			throw e;
		}
		return index;
	}

	@Transactional(readOnly = true)
	public SysFile getFileDetail(String id) {
		return sysFileDao.getFileDetail(id);
	}

	@Transactional(readOnly = true)
	public Long getFileCount(Map<String, Object> param) {
		return sysFileDao.getFileCount(param);
	}

	/**
	 * 如果request中包含多个文件，则文件名参数不会被使用，如果只有一个文件，则可以使用fileName为该上传的文件指定名称。
	 * @param request
	 * @param folder
	 * @param objectId
	 * @return
	 * @throws Exception
	 */
	private List<SysFile> saveFile(HttpServletRequest request,String folder,String objectId) throws Exception {
		List<MultipartFile> listFiles = UploadUtils.getUpFiles(request);
		List<SysFile> sysFilesList=new ArrayList<SysFile>();
		if(folder.startsWith("/")){
			folder=folder.substring(1);
		}
		if(!folder.endsWith("/")){
			folder=folder+"/";
		}
		String real=folder;
		String diyFolder = ConfigValues.getDiyFolderPath();
		folder = diyFolder+folder;
		// 一次遍历所有文件
		if(listFiles.size()>0){
			Date date;
			for (MultipartFile file : listFiles) {
				
				if (file != null) {
					String savePath = request.getServletContext().getRealPath("/");
					savePath = savePath.replaceAll("\\\\","/");
					if(!savePath.endsWith("/")){
						savePath = savePath+"/";
					}
					savePath += folder;
					new File(savePath).mkdirs();
					String name = FileUtils.getFileFirstName(file.getOriginalFilename());
					String type = FileUtils.getFileLastName(file.getOriginalFilename());
					String id = UUID.randomUUID().toString();
					String path = id+"."+type;
					while (new File(savePath + "/" + path).exists()) {
						id = UUID.randomUUID().toString();
						path = id+"."+type;
					}
					// 上传
					file.transferTo(new File(savePath + path));
					SysFile sysFile = new SysFile();
					sysFile.setFileName(name);
					sysFile.setFileType(type);
					sysFile.setFilePath(real+path);
					sysFile.setFileLength(file.getSize());
					sysFile.setId(id.toString());
					sysFile.setCreateTime(date=new Date());
					sysFile.setUpdateTime(date);
					sysFile.setObjectId(objectId);
					sysFileDao.saveFile(sysFile);
					sysFilesList.add(sysFile);
				}
			}
		}/*else if(listFiles.size()==1){
			MultipartFile file = listFiles.get(0);
			if (file != null) {
				String savePath = request.getServletContext().getRealPath("/");
				savePath = savePath.replaceAll("\\\\","/");
				if(savePath.endsWith("/")){
					savePath = savePath.substring(0,savePath.length()-1);
				}
				savePath += "/"+folder;
				new File(savePath).mkdirs();
				String fileType = "";
				if(StringUtils.isBlank(fileName)){
					fileName = file.getOriginalFilename();
					fileType = FileUtils.getFileLastName(fileName);
					fileName = FileUtils.getFileFirstName(fileName);
				}else{
					fileType = FileUtils.getFileLastName(fileName);
					if(StringUtils.isBlank(fileType)){
						fileType = FileUtils.getFileLastName(file.getOriginalFilename());
					}
				}
				String path = savePath + "/" + fileName;
				file.transferTo(new File(path+"."+fileType));
				SysFile sysFile = new SysFile();
				sysFile.setFileName(fileName);
				sysFile.setFileType(fileType);
				sysFile.setFilePath(folder+"/"+fileName+(StringUtils.isBlank(fileType)?"":("."+fileType)));
				sysFile.setFileLength(file.getSize());
				sysFile.setId(UUID.randomUUID().toString());
				sysFile.setCreateTime(new Date());
				sysFile.setUpdateTime(new Date());
				sysFile.setObjectId(objectId);
				sysFileDao.saveFile(sysFile);
				sysFilesList.add(sysFile);
			}
		}*/
		return sysFilesList;
	}
	private void rollbackFile(HttpServletRequest request,String folder,String fileName) throws Exception {
		List<MultipartFile> listFiles = UploadUtils.getUpFiles(request);
		// 一次遍历所有文件
		String realPath = request.getServletContext().getRealPath("/"+ConfigValues.getDiyFolderPath()).replaceAll("\\\\","/");
		if(!realPath.endsWith("/")){
			realPath = realPath+"/";
		}
		for (MultipartFile file : listFiles) {
			if (file != null) {
				String path ;
				if(StringUtils.isBlank(fileName)){
					path = realPath+folder;
				}else{
					path = realPath +folder+ "/" +fileName;
				}
				new File(path).delete();
			}
		}
	}
}
