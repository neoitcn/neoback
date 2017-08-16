package com.neo.sys.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LoginFailManagerUtil {

	/**
	 * 该map用于保存某ip频繁登录的问题
	 * key:ip地址
	 * value:一个Map集合：
	 * key:failNum 登录失败次数
	 * key:failDate 最后一次登录失败的时间
	 * key:keepHour 被锁定后的持续时间
	 */
	private static final Map<String,Map<String,Object>> loginFailMap = new HashMap<>(); 
	
	private static final int failTimes = 3; //限制失败登录次数为3次
	
	private static final int keepTime = 24*3600000; //被锁定的时长为24小时
	
	
	/**添加登录失败信息
	 * @param ip
	 * @param date
	 * keepHour 持续锁定的小时，0表示不锁定
	 */
	public final static void addFail(String ip,Date date){
		Map<String,Object> map = loginFailMap.get(ip);
		if(map == null){
			map = new HashMap<>();
			map.put("failNum", 0);
			loginFailMap.put(ip, map);
		}
		map.put("failNum", (Integer)map.get("failNum")+1);
		map.put("failDate", date);
		if(keepTime > 0){
			int failNum = (int) map.get("failNum");
			if(failNum >= failTimes){
				map.put("lock","locked");
			}
		}
	}
	
	/**
	 * 移除登录失败信息
	 * @param ip
	 */
	public final static void removeFail(String ip){
		loginFailMap.remove(ip);
	}
	
	/**
	 * 根据时间信息自动清除失败登录信息
	 * @param hour
	 */
	public final static void clearByTime(){
		long timestamp = System.currentTimeMillis();
		List<String> ipList = new LinkedList<>();
		for(String ip:loginFailMap.keySet()){
			Map<String,Object> map = loginFailMap.get(ip);
			Date date = (Date) map.get("failDate");
			long _t = date.getTime();
			if(timestamp - _t > keepTime){
				ipList.add(ip);
			}
		}
		for(String ip:ipList){
			loginFailMap.remove(ip);
		}
	}
	
	/**
	 * 判断登录失败信息是否存在 
	 * @param ip
	 * @return
	 */
	public final static boolean existsFail(String ip){
		return loginFailMap.get(ip) != null;
	}
	
	/**
	 * 获取当前ip登录失败次数
	 * @param ip
	 * @return
	 */
	public final static int getFailNum(String ip){
		Map<String,Object> map = loginFailMap.get(ip);
		if(map == null){
			return 0;
		}
		Integer num = (Integer) map.get("failNum");
		return num==null?0:num;
	}
	
	/**
	 * 获取登录失败的最后一次时间
	 * @param ip
	 * @return
	 */
	public final static Date getFailDate(String ip){
		Map<String,Object> map = loginFailMap.get(ip);
		if(map == null){
			return null;
		}
		return (Date) map.get("failDate");
	}
	
	/**
	 * 判断是否被锁定
	 * @param ip
	 * @return
	 */
	public final static boolean isLocked(String ip){
		Map<String,Object> map = loginFailMap.get(ip);
		if(map == null){
			return false;
		}
		return map.get("lock") != null;
	}
	
}
