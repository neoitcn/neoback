package com.neo.sys.service.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.sys.dao.SequenceDao;
import com.neo.sys.entity.bean.Sequence;
import com.neo.sys.service.BaseService;

/**
 * 这是一个序列service，适合访问量较小的场景。如果需要高并发需求，则需要使用Redis做支持

 */
@Service
public class SequenceService{

	@Autowired
	private SequenceDao sequenceDao;
	
	@Transactional(readOnly=true)
	public Sequence getSequenceById(Integer id) {
		return sequenceDao.getSequenceById(id);
	}

	/**
	 * 获取当前序列，但是不会自增
	 * @param key
	 * @return
	 */
	@Transactional(readOnly=true)
	public Sequence getSequenceByKey(String key) {
		return sequenceDao.getSequenceByKey(key);
	}
	/**
	 * 获取当前序列值，然后对序列自增inc
	 * 如果序列不存在则返回null
	 * 如果序列的当前值为空则返回null
	 * @param key
	 * @param inc
	 * @return
	 */
	@Transactional(readOnly=false)
	public String getSequenceByKeyAndIncBy(String key,int inc) throws Exception{
		Sequence sequence = getSequenceByKey(key);
		if(sequence == null){
			throw new Exception("序列不存在");
		}
		if(StringUtils.isBlank(sequence.getSequenceNum())){
			sequence.setSequenceNum(String.valueOf(0));
		}
		String returnNum = sequence.getSequenceNum();
		Long l = Long.parseLong(sequence.getSequenceNum());
		l = l+inc;
		String strL = String.valueOf(l);
		
		//如果需要为自增后的结果前面补空位0
		if(strL.length()<sequence.getSequenceNum().length()){
			int div = sequence.getSequenceNum().length() - strL.length();
			for(int i=0;i<div;i++){
				strL = "0"+strL;
			}
		}
		setSequence(sequence.getId(),strL);
		return returnNum;
	}
	/**
	 * 创建一个sequence，
	 * 可以自定义初始数值进行自增，支持0开头的数字
	 * @param sequence
	 * @return
	 */
	@Transactional(readOnly=false)
	public int addSequence(String name,String key,String initNum) throws Exception{
		Sequence s = getSequenceByKey(key);
		if(StringUtils.isBlank(initNum)){
			throw new Exception("请为序列指定初始值");
		}
		if(!initNum.matches("\\d{1,12}")){
			throw new Exception("序列只支持包含0的正整数，长度为1-12位");
		}
		if(s!=null){
			throw new Exception("该序列已存在");
		}
		s = new Sequence();
		s.setName(name);
		s.setKeyword(key);
		s.setSequenceNum(initNum);
		return sequenceDao.addSequence(s);
	}
	/**
	 * 修改序列的基本属性，但是序列号不会被影响
	 * @param sequence
	 * @return
	 */
	@Transactional(readOnly=false)
	public int updateSequence(Sequence sequence) {
		return sequenceDao.updateSequence(sequence);
	}
	
	
	/**
	 * 当使用了以0开头的序列，你不应该调用这个方法来执行自增，因为这样会导致你的0丢失
	 * 该方法不会返回自增后的结果，而是返回操作记录的条数
	 * @param id
	 * @param sequenceNum 要自增的数字，比如此次要自增1则传入1
	 * @return
	 */
	@Transactional(readOnly=false)
	private int incSequence(Integer id,Long sequenceNum){
		return sequenceDao.incSequence(id, sequenceNum);
	}
	
	/**
	 * 清除序列序号，清除后序号为0
	 * @param key
	 * @return
	 */
	@Transactional(readOnly=false)
	public int clearSequence(String key){
		return sequenceDao.clearSequence(key);
	}
	
	/**
	 * 手动重新设置序列
	 * @param id
	 * @return
	 */
	@Transactional(readOnly=false)
	public int setSequence(Integer id,String sequenceNum) throws Exception{
		if(sequenceNum == null || !sequenceNum.matches("\\d{1,12}")){
			throw new Exception("序列只支持包含0的正整数，长度为1-12位");
		}
		return sequenceDao.setSequence(id,sequenceNum);
	}
}
