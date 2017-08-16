//package com.neoit.mapper.sys;
//
//import java.util.List;
//
//import org.apache.ibatis.annotations.Param;
//
//import com.neoit.entity.sys.Office;
//import com.neoit.sys.annotation.MyBatisDao;
//
///**
// * 终端列表Dao层
// *
// */
//@MyBatisDao
//public interface OfficeDao {
//
//	/**
//	 * checkDisable和officeId同时出现，当需要检查当前的树节点是否 应该禁用，应该传入一个id，以保证传入的树id会被禁用掉。
//	 * @param enabled
//	 * @param showLast
//	 * @param checkDisable
//	 * @param officeId
//	 * @return
//	 */
//	public List<Office> getAllOffices(@Param("enabled") int enabled,@Param("showLast") Integer showLast,@Param("checkDisable") String checkDisable,@Param("roleId") Integer roleId);
//	
//	public Office getOfficeByCode(String code);
//	
//	public Office getOfficeByName(String name);
//	
//	public List<Office> getOfficeChildren(@Param("id") Integer id);
//	
//	public List<Office> getOfficeChildrenAndParent(@Param("id") Integer id);
//	
//	public List<Office> getOfficeParent(@Param("id") Integer id);
//	
//	public List<Office> getLastOffice(@Param("enabled") int enabled);
//	
//	public Office getOfficeById(Integer id);
//	
//	public int addOffice(Office officeTree);
//	
//	public int updateOffice(Office officeTree);
//	
//	public int deleteOffice(Integer id);
//	
//}
