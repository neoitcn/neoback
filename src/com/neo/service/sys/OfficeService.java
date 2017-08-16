//package com.neoit.service.sys;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import com.neoit.sys.emue.SessionAttribute;
//import com.neoit.sys.entity.bean.SysFile;
//import com.neoit.sys.utils.*;
//import com.neoit.entity.sys.Office;
//import com.neoit.entity.sys.User;
//import com.neoit.mapper.sys.OfficeDao;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
////import com.neoit.entity.docm.Drugster;
////import com.neoit.entity.docm.Prisoner;
////import com.neoit.mapper.docm.DrugsterDao;
////import com.neoit.mapper.docm.PrisonerDao;
//import com.neoit.sys.service.BaseService;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 当涉及到依照树结构获取权限时，如果勾选的是某节点的的上级节点，则可以通过这样的方式判断是否合法：
// * 
// * WHERE ID LIKE concat('ID_VALUE','%');
// * 
// * 这样可以查询出它的本身以及它的所有符合条件的下级子节点。
// * 
// */
//@Service
//public class OfficeService{
//
//	@Autowired
//	private OfficeDao officeDao;
//
////	@Autowired
////	private DrugsterDao drugsterDao;
//
//	@Transactional(readOnly = true)
//	public List<Office> getAllOffices(int enabled, Integer showLast, String checkDisable, Integer roleId)
//			throws Exception {
//
//		return officeDao.getAllOffices(enabled, showLast, checkDisable, roleId);
//
//	}
//
//	/**
//	 * noCheck:0:默认不加载该属性 1:全加载为不显示 2中队显示人不显示 3人显示中队不显示
//	 * @param noCheck
//	 * @return
//	 * @throws Exception
//	 */
////	@Transactional(readOnly = true)
////	public List<Map<Object, Object>> getAllOfficesPrisoner( String name, String target,int noCheck)
////			throws Exception {
////		name = name == null ? "" : name;
////		List<Office> officeList;
////		if(AccessUtil.isAdmin()){
////			officeList = getAllOffices(1, null, null, null);
////		}else{
////			officeList = AccessUtil.helpCreateCurrentDataChildOffice();
////			officeList.addAll(AccessUtil.helpCreateCurrentDataParentOfficeWithoutOwn());//这句话一加就是树形列表显示：本级及包含的子集和上级，不加就是：本级及包含的子集
////		}
////		//List<Prisoner> prisonerList = null;
////		List<Drugster> prisonerList = null;
////		int length = officeList.size();
////		List<Map<Object, Object>> list = officeList.stream().map(office->{
////			office.setNocheck(true);
////			Map<Object, Object> map = new HashMap<>();
////			map.put("id", office.getId());
////			map.put("parentId", office.getParentId());
////			map.put("parentIds", office.getParentIds());
////			map.put("name", office.getName());
////			map.put("allowGroup", office.getAllowGroup() == 1);
////			if (noCheck == 1 || noCheck == 3) {
////				map.put("nocheck", true);
////			}
////			map.put("size", 0);
////			map.put("onlineSize", 0);
////			return map;
////		}).collect(Collectors.toList());
////		Map<Integer,Office> officeMap = officeList.stream().collect(Collectors.toMap(o->o.getId(),o->o));
////		List<Map<String, Object>> prisonerListTemp = new ArrayList<>();
////		//prisonerList = prisonerDao.getPrisonerByOffice(oids.toString());
////		prisonerList = drugsterDao.getDrugsterAdbByOffice(officeList);
////		if(prisonerList == null){
////			prisonerList = new ArrayList<>();
////		}
////		final String _name = name;
////		prisonerList = prisonerList.stream().sequential().filter(p->{
////			if(p.getName().contains(_name)){
////				if(target != null && "2".equals(target)){
////					return "1".equals(p.getOnline());
////				}
////				return true;
////			}
////			return false;
////		}).collect(Collectors.toList());
////		Map<Integer,Office> tempOfficeMap = officeList.stream().collect(Collectors.toMap(o->o.getId(),o->o));
////		List<Map<Object,Object>> listPersonResult =
////				prisonerList
////						.stream()
////						.sequential()
////						.filter(p->officeList.stream().anyMatch(o->o.getId().equals(p.getOfficeId())))
////						.filter(p->StringUtils.isNotBlank(p.getImei()) || StringUtils.isNotBlank(p.getDbMobileNo()))
////						.map(p->{
////							Map<Object, Object> map = new HashMap<>();
////							Office office = tempOfficeMap.get(p.getOfficeId());
////							map.put("id", p.getId());
////							map.put("parentId", p.getOfficeId());
////							map.put("parentIds", office.getId() + "," + office.getParentIds());
////							map.put("name", p.getName());
////							map.put("idCard", p.getIdCard());
////							map.put("phone", p.getPhone());
////							map.put("imei", p.getImei());
////							map.put("persontype", p.getPersontype());
////							map.put("online", p.getOnline());
////							//map.put("skOnline", "03".equals(p.getSkOnline()) ? "1" : "0");
////							map.put("skOnline", p.getOnline());//2017-03-14 黄金晶 改
////							map.put("hasDBPhone", StringUtils.isBlank(p.getDbMobileNo()) ? 0 : 1);//是否有已订购的手机，用于区分二者皆不在线时应显示哪种图标。
////							map.put("hasImei", StringUtils.isBlank(p.getImei()) ? 0 : 1);
////							if (noCheck == 1 || noCheck == 2) {
////								map.put("nocheck", true);
////							}
////							map.put("prisonerId",p.getId() + "");
////							String photoPath = p.getPhotoPath();
////							if(StringUtils.isNotBlank(photoPath)){
////								map.put("filepath", ConfigValues.getDiyFolderMapper()+photoPath);
////							}
////							map.put("onlineSize","1".equals(p.getOnline())||"03".equals(p.getSkOnline())?1:0);
////							map.put("size",1);
////							return map;
////						})
////						.parallel()
////						.collect(Collectors.toList());
////
////		NodeCreateUtil.countAddSliderNodeForLast(list,listPersonResult,"id","parentId",(isFirst, fieldName, lastValue, currentValue, lastObj, currentObj)->{
////			switch((String)fieldName){
////				case "onlineSize":
////					Map<Object,Object> map = (Map<Object, Object>) currentObj;
////					String online = (String) map.get("online");
////					if(online == null || !"1".equals(online)){
////						online = (String) map.get("skOnline");
////					}
////					if(online == null){
////						if(isFirst){
////							return NodeCreateUtil.getOrDefault(currentValue,0);
////						}else{
////							return (int)NodeCreateUtil.getOrDefault(lastValue,0)+(int)NodeCreateUtil.getOrDefault(currentValue,0);
////						}
////					}else{
////						if("1".equals(online)){
////							if(isFirst){
////								return NodeCreateUtil.getOrDefault(currentValue,1);
////							}else{
////								return (int)NodeCreateUtil.getOrDefault(lastValue,0)+1;
////							}
////						}else{
////							if(isFirst){
////								return NodeCreateUtil.getOrDefault(currentValue,0);
////							}else{
////								return (int)NodeCreateUtil.getOrDefault(lastValue,0);
////							}
////						}
////					}
////				case "size":
////					map = (Map<Object, Object>) currentObj;
////					if(map.get("idCard") != null){
////						return isFirst?1:(int)lastValue+1;
////					}else{
////						return isFirst?currentValue:(int)NodeCreateUtil.getOrDefault(lastValue,0)+(int)NodeCreateUtil.getOrDefault(currentValue,0);
////					}
////			}
////			return null;
////		},"size","onlineSize");
//
//		/*if (prisonerList != null){
//			 String photoPath;
//			Office office;
//			for (int j = 0; j < prisonerList.size(); j++) {
//				office = officeMap.get(prisonerList.get(j).getOfficeId());
//				if(office == null){
//					continue;
//				}
//				Drugster p = prisonerList.get(j);
//				//既没有imei电子手铐也没有订购的手机号，则不在树上显示
//				if (StringUtils.isBlank(p.getImei()) && StringUtils.isBlank(p.getDbMobileNo())) {
//					continue;
//				}
//				//Prisoner p = prisonerList.get(j);
//				Map<String, Object> map = new HashMap<>();
//				map.put("id", p.getId());
//				map.put("parentId", office.getId());
//				map.put("parentIds", office.getId() + "," + office.getParentIds());
//				map.put("name", p.getName());
//				map.put("idCard", p.getIdCard());
//				map.put("phone", p.getPhone());
//				map.put("imei", p.getImei());
//				map.put("persontype", p.getPersontype());
//				map.put("online", p.getOnline());
//				map.put("skOnline", "03".equals(p.getSkOnline()) ? 1 : 0);
//				map.put("hasDBPhone", StringUtils.isBlank(p.getDbMobileNo()) ? 0 : 1);//是否有已订购的手机，用于区分二者皆不在线时应显示哪种图标。
//				map.put("hasImei", StringUtils.isBlank(p.getImei()) ? 0 : 1);
//				if (noCheck == 1 || noCheck == 2) {
//					map.put("nocheck", true);
//				}
//				map.put("prisonerId", prisonerList.get(j).getId() + "");
//				photoPath = p.getPhotoPath();
//				if(StringUtils.isNotBlank(photoPath)){
//					map.put("filepath", ConfigValues.getDiyFolderMapper()+photoPath);
//				}
//				//map.put("filepath", p.getFilePath());
//				prisonerListTemp.add(map);
//				list.add(map);
//			}
//		}
//		// 增加人数显示
//		List<Map<String, Object>> tempList = new ArrayList<>();
//		int pTempNum = 0;// 防止某人员不存在于所有节点下出现的死循环问题
//		// 首先将所有人的人数先挂载到最低级的机构下面
//		while (!prisonerListTemp.isEmpty() && pTempNum != prisonerListTemp.size()) {
//			pTempNum = prisonerListTemp.size();
//			here: for (int i = 0; i < prisonerListTemp.size();) {
//				Map<String, Object> m = prisonerListTemp.get(i);
//				for (Map<String, Object> m2 : list) {
//					Integer pId = (Integer) m.get("parentId");
//					if (pId == m2.get("id")) {
//						Integer size = (Integer) m2.get("size");
//						Integer onlineSize = (Integer) m2.get("onlineSize");
//						m2.put("size", ++size);
//						if(m.get("online") !=null && "1".equals(m.get("online"))){
//							m2.put("onlineSize", ++onlineSize);
//						}
//						if("1".equals(target)){
//							if(m.get("name").toString().contains(name) || m.get("phone").toString().contains(name)){
//								m2.put("search", "true");
//								m.put("search", "true");
//							}
//						}else if("2".equals(target)){
//							if(m.get("online") != null && "1".equals(m.get("online"))){
//								if(m.get("name").toString().contains(name) || m.get("phone").toString().contains(name)){
//									m2.put("search", "true");
//									m.put("search", "true");
//								}
//							}
//						}
//						prisonerListTemp.remove(i);
//						boolean b = true;
//						for (Object o : tempList) {
//							if (o.equals(m2)) {
//								b = false;
//								break;
//							}
//						}
//
//						if (b)
//							tempList.add(m2);
//						continue here;
//					}
//				}
//				i++;
//			}
//		}
//
//		// 循环遍历所有的机构，累计依次叠加。
//		List<Map<String, Object>> tempList2 = new ArrayList<>();
//		while (!tempList.isEmpty()) {
//			pTempNum = 0;
//			boolean isExit = true;
//			while (tempList.size() != pTempNum && !tempList.isEmpty()) {
//				pTempNum = tempList.size();
//				for (int i = 0; i < list.size(); i++) {
//					for (int j = 0; j < tempList.size();) {
//						if (list.get(i).get("id")==tempList.get(j).get("parentId")) {
//							Map<String, Object> m = list.get(i);
//							m.put("size", (Integer) m.get("size") + (Integer) tempList.get(j).get("size"));
//							m.put("onlineSize", (Integer) m.get("onlineSize") + (Integer) tempList.get(j).get("onlineSize"));
//							if(tempList.get(j).get("search") != null)
//								m.put("search", tempList.get(j).get("search"));
//							boolean b = true;
//							for (Object o : tempList2) {
//								if (o.equals(m)) {
//									b = false;
//									break;
//								}
//							}
//							if (b)
//								tempList2.add(m);
//							tempList.remove(j);
//							isExit = false; //如果没走到这一步，说明能够遍历的都已经完成，其余的是没有关联的数据
//							continue;
//						}
//						j++;
//					}
//				}
//			}
//			if(isExit){
//				break;
//			}
//			tempList.clear();
//			tempList.addAll(tempList2);
//			tempList2.clear();
//		}
//
//		if (StringUtils.isNotBlank(name)) {
//			//过滤数据
//			for(int i=0;i<list.size();){
//				if(list.get(i).get("search") == null){
//					list.remove(i);
//					continue;
//				}
//				list.get(i).remove("search");
//				i++;
//			}
//		}else{
//			if("2".equals(target)){
//				Map<String,Object> map;
//				for(int i=0;i<list.size();){
//					if((map=list.get(i)).get("onlineSize")==null&&(map.get("online")== null || !map.get("online").equals("1")) && (map.get("skOnline")==null || !map.get("skOnline").equals("1"))){
//						list.remove(i);
//						continue;
//					}
//					i++;
//				}
//			}
//		}*/
//		//return list;
//
//	//}
//
//	/**
//	 * 获取当前机构的子机构，不包括自己
//	 * @param id
//	 * @return
//	 */
//	@Transactional(readOnly = true)
//	public List<Office> getOfficeChildren(Integer id) {
//		return officeDao.getOfficeChildren(id);
//	}
//
//	/**
//	 * 获取当前机构的子机构和它的父机构，包括自己
//	 * @param id
//	 * @return
//	 */
//	@Transactional(readOnly = true)
//	public List<Office> getOfficeChildrenAndParent(Integer id) {
//		return officeDao.getOfficeChildrenAndParent(id);
//	}
//	/**
//	 * 获取当前机构的父机构和它的父机构，但不包括自己
//	 * @param id
//	 * @return
//	 */
//	@Transactional(readOnly = true)
//	public List<Office> getOfficeParent(Integer id) {
//		return officeDao.getOfficeParent(id);
//	}
//
//	@Transactional(readOnly = true)
//	public Office getOfficeById(Integer id) {
//		return officeDao.getOfficeById(id);
//	}
//	
//	@Transactional(readOnly = true)
//	public Office getOfficeByName(String name) {
//		return officeDao.getOfficeByName(name);
//	}
//
//	@Transactional(readOnly = true)
//	public Office getOfficeByCode(String code) {
//		return officeDao.getOfficeByCode(code);
//	}
//
//	@Transactional(readOnly = true)
//	public List<Office> getLastOffice(int enabled) {
//		return officeDao.getLastOffice(enabled);
//	}
//
//	@Transactional(readOnly = false)
//	public int addOffice(Office officeTree) {
//		AutoSetColumn.autoInsert(officeTree);
//		return officeDao.addOffice(officeTree);
//	}
//
//	@Transactional(readOnly = false)
//	public int updateOffice(Office officeTree) {
//		AutoSetColumn.autoUpdate(officeTree);
//		return officeDao.updateOffice(officeTree);
//	}
//
//	@Transactional(readOnly = false)
//	public int deleteOffice(Integer id) {
//		return officeDao.deleteOffice(id);
//	}
//
//	@Transactional (readOnly = false)
//	public void saveOffice(Office office, HttpServletRequest request) throws Exception{
//		Integer pId = office.getParentId();
//		Office pOffice = getOfficeById(pId);
//		if (pOffice != null && pOffice.getAllowGroup() == 0) {
//			throw new Exception(pOffice.getName() + "机构下不能再添加子机构！");
//		}else{
//			String pids = pOffice.getParentIds();
//			if(StringUtils.isNotBlank(pids)){
//				String pidsArr[] = pids.split(",");
//				if(pidsArr.length>6){
//					throw new Exception("该机构已经是最小单位，不能再添加子机构了");
//				}else if(pidsArr.length == 6){
//					office.setAllowGroup(0);
//				}
//			}
//		}
//		if (null == office.getId()) {
//			office.setCode(SequenceUtil.getSequenceYearNumAndIncBy("OFFICE", 1));
//			addOffice(office);
//		} else {
//			updateOffice(office);
//		}
//
//		//保存完成后需要更新session中当前用户的机构，如果需要的话。
//		User user = (User) request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
//		if(user.getId()!=null && user.getOfficeId()!=null){
//			if(user.getOfficeId() == office.getId()){
//				request.getSession().setAttribute(SessionAttribute.USER_CURRENT_OFFICE,office);
//				user.setOffice(office);
//			}
//			List<Office> child = (List<Office>) request.getSession().getAttribute(SessionAttribute.USER_CHILD_OFFICE);
//			List<Office> parent = (List<Office>) request.getSession().getAttribute(SessionAttribute.USER_PARENT_OFFICE);
//			if(child != null){
//				boolean is = child.stream().anyMatch(o->user.getOfficeId()==o.getId());
//				if(is){
//					request.getSession().setAttribute(SessionAttribute.USER_CHILD_OFFICE,getOfficeChildren(user.getOfficeId()));
//				}
//			}
//			if(parent != null){
//				boolean is = parent.stream().anyMatch(o->user.getOfficeId()==o.getId());
//				if(is){
//					request.getSession().setAttribute(SessionAttribute.USER_PARENT_OFFICE,getOfficeParent(user.getOfficeId()));
//				}
//			}
//		}
//	}
//
//	@Transactional (readOnly = false)
//	public void deleteOfficeById(Integer id,HttpServletRequest request) throws Exception{
//		List<Office> listOffice = getOfficeChildren(id);
//		if (listOffice != null && listOffice.size() > 1) {
//			throw new Exception("该机构下有子机构，不能直接删除！");
//		}
//		deleteOffice(id);
//		User user = (User) request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
//		if(user.getId()!=null && user.getOfficeId()!=null){
//			List<Office> child = (List<Office>) request.getSession().getAttribute(SessionAttribute.USER_CHILD_OFFICE);
//			if(child != null){
//				boolean is = child.stream().anyMatch(o->user.getOfficeId()==o.getId());
//				if(is){
//					request.getSession().setAttribute(SessionAttribute.USER_CHILD_OFFICE,getOfficeChildren(user.getOfficeId()));
//				}
//			}
//		}
//	}
//}
