package com.neo.sys.utils;

import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

import com.neo.sys.entity.bean.Sequence;
import com.neo.sys.service.service.SequenceService;

/**
 *本序列操作只适用于并发量小的场景，基于SequenceService操作，你不应该直接操作SequenceService。
 */
public final class SequenceUtil {
	private final static SequenceService sequenceService = SpringUtils.getApplicationBean(SequenceService.class);
	private final static ReentrantLock lock = new ReentrantLock();
	/**
	 * 获取序列信息
	 * @param key
	 * @return
	 */
	public final static Sequence getSequenceInfo(String key){
		return sequenceService.getSequenceByKey(key);
	}
	
	/**
	 * 获取当前序列值，不推荐在此设置序列信息后保存，详细参见getSequenceYearNumAndIncBy方法给出的说明
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public final static String getSequenceNum(String key) throws Exception{
		Sequence sequence = sequenceService.getSequenceByKey(key);
		if(sequence == null){
			throw new Exception("序列不存在");
		}
		String numStr = sequence.getSequenceNum();
		if(numStr == null || !numStr.matches("\\d{1,12}")){
			throw new Exception("序列值存在问题");
		}
		return numStr;
	}
	
	/**
	 * 添加一个新的序列
	 * 该方法在Spring初始化完成时调用，避免了线程并发问题。
	 * 该方法不应该在Spring初始化完成后调用
	 * @param key
	 * @param name
	 * @param sequenceNum
	 * @throws Exception
	 */
	@Deprecated
	public final static void addSequence(String key,String name,String sequenceNum) throws Exception{
		sequenceService.addSequence(name, key, sequenceNum);
	}
	
	/**
	 * 获取了序列之后再自增
	 * @param key
	 * @param inc
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public final static String getSequenceNumAndIncBy(String key,int inc) throws Exception{
		return sequenceService.getSequenceByKeyAndIncBy(key, inc);
	}
	
	/**
	 * 清除序列值，清除后值为0
	 * @param key
	 * @throws Exception
	 */
	@Deprecated
	public final static void clearSequence(String key) throws Exception{
		sequenceService.clearSequence(key);
	}
	
	/**
	 * 修改序列基本属性，不包括序列序号
	 * @param sequence
	 * @throws Exception
	 */
	public final static void setSequenceBaseInfo(Sequence sequence) throws Exception{
		sequenceService.updateSequence(sequence);
	}
	
	/**
	 * 修改序列的序列号
	 * @param key
	 * @param sequenceNum
	 * @throws Exception
	 */
	@Deprecated
	public final static void setSequenceNum(String key,String sequenceNum) throws Exception{
		Sequence sequence = sequenceService.getSequenceByKey(key);
		if(sequence == null){
			throw new Exception("序列不存在");
		}
		sequenceService.setSequence(sequence.getId(), sequenceNum);
	}
	
	/**
	 * 返回一个序列号，返回后自动自增inc，但返回的序列前面总是带有一个年。
	 * 如果年份变更，则会清除之前的序列为0，然后年份加一继续递增。递增结果从1开始
	 * 该方法在调用前，需要先初始化该序列，调用addSequence可以指定应有的规则为例如 201600001，否则将会出现未知情况
	 * 该方法具备一个独立的事务或者称作锁，调用该方法会锁定方法，相当于锁定了整张表，但仅限于该方法，在调用该方法时不会真的锁数据表。所以在你调用了其他方法操作序列时，应该考虑到事务的安全性。
	 * 当程序中使用了该方法操作序列时，推荐不应该使用其他方法操作，或者操作同一个序列时应该在同一个事物或锁内进行。
	 * @param key 指定的名称
	 * @param inc 
	 * @param initNum
	 * @throws Exception
	 */
	public final static String getSequenceYearNumAndIncBy(String key,int inc) throws Exception{
		final ReentrantLock inLock = lock;
		try{
			inLock.lock();
			
			String num = getSequenceNum(key);
			String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			int div = num.length() - String.valueOf(year).length();
			if(div <= 0){
				throw new Exception("该序列不适合此规则");
			}
			String returnResult = num;
			if(!num.startsWith(String.valueOf(year))){
				num = String.valueOf(0);
				returnResult = null;
			}else{
				num = String.valueOf(Long.parseLong(num.substring(year.length()))+inc);
			}
			if(num.length()<div){
				int d = div - num.length();
				for(int i=0;i<d;i++){
					num = "0" + num;
				}
			}
			setSequenceNum(key, year+num);
			if(returnResult == null){
				returnResult = year+num;
			}
			return returnResult;
		}catch(Exception e){
			throw e;
		}finally{
			inLock.unlock();
		}
	}
}
