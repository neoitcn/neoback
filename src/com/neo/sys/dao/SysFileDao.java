package com.neo.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neo.sys.annotation.MyBatisDao;
import com.neo.sys.entity.bean.SysFile;

@MyBatisDao
public interface SysFileDao {

	public List<SysFile> getFiles(Map<String,Object> param);
	
	public List<SysFile> getFileByObjectId(String objectId);
	
	public int saveFile(SysFile file);
	
	public int deleteFile(@Param("id") String id);
	
	public int deleteFileByObjectId(@Param("objectId") String objectId);
	
	public SysFile getFileDetail(@Param("id") String id);
	
	public Long getFileCount(Map<String,Object> param);
}

