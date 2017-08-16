package com.neo.sys.service.service;

import java.util.List;
import java.util.Map;

import com.neo.sys.utils.AutoSetColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.sys.dao.CodeDao;
import com.neo.sys.entity.bean.Code;
import com.neo.sys.entity.bean.CodeValue;
import com.neo.sys.service.BaseService;

@Service
public class CodeService{

	@Autowired
	private CodeDao codeDao;

	@Transactional(readOnly = true)
	public List<Code> getAllCode(int enabled) {
		return codeDao.getAllCode(enabled);
	}

	@Transactional(readOnly = true)
	public List<CodeValue> getValuesByCodeId(int codeId, int enabled) {
		return codeDao.getValuesByCodeId(codeId, enabled);
	}

	@Transactional(readOnly = true)
	public CodeValue getCodeValue(String key, String value) {
		return codeDao.getCodeValue(key, value);
	}

	@Transactional(readOnly = false)
	public int addCode(Code code) {
		AutoSetColumn.autoInsert(code);
		return codeDao.saveCode(code);
	}

	@Transactional(readOnly = false)
	public int addCodeValue(CodeValue codeValue) {
		AutoSetColumn.autoInsert(codeValue);
		return codeDao.saveCodeValue(codeValue);
	}

	@Transactional(readOnly = false)
	public int updateCode(Code code) {
		AutoSetColumn.autoUpdate(code);
		return codeDao.updateCode(code);
	}

	@Transactional(readOnly = false)
	public int updateCodeValue(CodeValue code) {
		AutoSetColumn.autoUpdate(code);
		return codeDao.updateCodeValue(code);
	}

	@Transactional(readOnly = false)
	public int deleteCodeById(int id) {
		return codeDao.deleteCodeById(id);
	}

	@Transactional(readOnly = false)
	public int deleteCodeValueById(Integer id) {
		return codeDao.deleteCodeValueById(id);
	}
	
	@Transactional(readOnly = false)
	public int deleteCodeValueByCodeId(Integer id) {
		return codeDao.deleteCodeValueById(id);
	}

	@Transactional(readOnly = true)
	public Code getCodeById(int id) {
		return codeDao.getCodeById(id);
	}

	@Transactional(readOnly = true)
	public CodeValue getCodeValueById(int id) {
		return codeDao.getCodeValueById(id);
	}

	@Transactional(readOnly = true)
	public Code getCodeByKey(String key) {
		return codeDao.getCodeByKey(key);
	}

	@Transactional(readOnly = true)
	public List<Code> getCodes(Map<String,Object> param) {
		// TODO Auto-generated method stub
		return codeDao.getCodes(param);
	}
	@Transactional(readOnly = true)
	public Long getCodeCount(Map<String,Object> param) {
		// TODO Auto-generated method stub
		return codeDao.getCodeCount(param);
	}
	@Transactional(readOnly = true)
	public Long getCodeValueCount(Map<String,Object> param) {
		// TODO Auto-generated method stub
		return codeDao.getCodeValueCount(param);
	}

	@Transactional(readOnly=true)
	public List<CodeValue> getCodeValues(Map<String,Object> param){
		return codeDao.getCodeValues(param);
	}
}
