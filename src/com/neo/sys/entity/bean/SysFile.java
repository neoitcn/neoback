package com.neo.sys.entity.bean;

import java.io.Serializable;

import com.neo.sys.utils.ConfigValues;
import org.apache.commons.lang.StringUtils;

import com.neo.sys.entity.BaseBean;

public class SysFile extends BaseBean implements Serializable {

	private static final long serialVersionUID = 4263207533693727405L;

	private String id;
	private String fileName;
	private String fileType;
	private Long fileLength;
	private String filePath;
	private String objectId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileLength() {
		return fileLength;
	}

	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}

	/**
	 * 该方法获取截去配置路径后用户定义的文件夹
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * 获取对外用户下载的链接地址
	 * @return
	 */
	public String getDownloadFilePath(){

		return ConfigValues.getDiyFolderMapper()+filePath;
	}

	/**
	 * 通过该路径可以找到文件所在服务器的真是物理地址
	 * @return
	 */
	public String getLocationFilePath(){
				return ConfigValues.getDiyFolderPath()+filePath;
	}

}
