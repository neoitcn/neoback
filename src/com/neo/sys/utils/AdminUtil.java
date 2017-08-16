package com.neo.sys.utils;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.neo.entity.Sys_user;
import com.neo.sys.entity.bean.Admin;

/**
 * 该类用于判断和获取管理员的权限问题。
 * 
 *
 */
public class AdminUtil {

	/**
	 * 管理员信息
	 */
	private static Map<String, Admin> securityMap = new HashMap<>();

	static {
		// 配置中的用户名
		String user = SystemConfigUtil.getConfig("admin.userName");
		//配置中的用户姓名
		String name = SystemConfigUtil.getConfig("admin.name");
		// 配置中的密码
		String password = SystemConfigUtil.getConfig("admin.password");
		// 配置中的验证码
		String validateCode = SystemConfigUtil.getConfig("admin.validateCode");
		// 配置中的权限
		String security = SystemConfigUtil.getConfig("admin.security");

		if (StringUtils.isNotBlank(user)) {
			if (StringUtils.isNotBlank(password)) {
				String users[] = user.split(",");
				String passwords[] = password.split("\\),\\(");
				String names[] = name.split(",");
				if (users.length != passwords.length ) {
					System.err.println("管理员初始化失败，错误原因：用户名和密码不对称。");
				} else if(names.length!=users.length){
					System.err.println("管理员初始化失败，错误原因：用户名和用户姓名不对称。");
				}else {
					String validateCodes[] = null;
					if (StringUtils.isNotBlank(validateCode)) {
						validateCodes = validateCode.split(",");
					}
					if (validateCodes != null && validateCodes.length > 1 && validateCodes.length != users.length) {
						System.err.println("管理员初始化失败，错误原因：用户与验证码不对称。");
					} else {
						if (StringUtils.isBlank(security)) {
							System.err.println("管理员初始化失败，错误原因：没有为用户配置权限。");
						} else {
							String securities[] = security.split(";");
							if (securities.length != users.length) {
								System.err.println("管理员初始化失败，错误原因：不能确定用户具备哪些权限。");
							} else {
								if(passwords.length>1){
									for (int i = 0; i < users.length; i++) {
										if (i % 2 == 0){
											if (!passwords[i].startsWith("(")) {
												System.err.println("管理员初始化失败(已初始化的管理员有" + i + "个)，错误原因：用户的密码没有用()包裹。");
												break;
											}
											passwords[i] = passwords[i].substring(1);
										}else{
											if (!passwords[i].endsWith(")")) {
												System.err.println("管理员初始化失败(已初始化的管理员有" + i + "个)，错误原因：用户的密码没有用()包裹。");
												break;
											}
											passwords[i] = passwords[i].substring(0,passwords[i].length()-1);
										}
										if (StringUtils.isBlank(passwords[i])) {
											System.err.println("管理员初始化失败(已初始化的管理员有" + i + "个)，错误原因：用户密码不能为空。");
											break;
										}
									}
								}else{
									passwords[0] = passwords[0].substring(1, passwords[0].length()-1);
								}
								for (int i = 0; i < users.length; i++) {
									String u = users[i];
									String n = names[i];
									String p = passwords[i];
									if(StringUtils.isBlank(p)){
										System.err.println("管理员密码不可为空，必须为其指定！");
										break;
									}
									String v = null;
									if (validateCodes != null) {
										if (validateCodes.length == 1) {
											v = validateCodes[0];
										} else {
											v = validateCodes[i];
										}
										v = StringUtils.isBlank(v) ? null : v;
									}
									String s = securities[i];
									if (s.startsWith("(") && s.endsWith("")) {
										String se = s.substring(1, s.length() - 1);
										if (StringUtils.isBlank(se)) {
											System.err.println("管理员初始化失败(已初始化的管理员有" + i + "个)，错误原因：用户的权限没有被指定。");
										} else {
											String ses[] = se.split(",");
											List<String> list = new ArrayList<>();
											for (String str : ses) {
												if ("*".equals(str)) {
													list = null;
													break;
												}
												list.add(str);
											}
											Admin admin = new Admin();
											admin.setName(u);
											admin.setPassword(p);
											admin.setValidateCode(v);
											admin.setCnName(n);
											admin.setControllerSecurity(list);
											securityMap.put(u, admin);
										}
									} else {
										System.err.println("管理员初始化失败(已初始化的管理员有" + i + "个)，错误原因：用户的权限没有用()包裹。");
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static boolean isExist(String userName) {
		Admin admin = securityMap.get(userName);
		return admin!=null;
	}

	public static void updateAdmin(String oldUserName,String userName,String name,String password) throws Exception{
		Admin admin = securityMap.get(oldUserName);
		if(admin == null){
			return;
		}
		if(userName!=null){
			admin.setName(userName);
		}
		if(name !=null){
			admin.setCnName(name);
		}
		if(password!=null){
			admin.setPassword(password);
		}
		List<String> security;
		String code = "";
		Properties p = new Properties();
		for(Admin ad:securityMap.values()){
			userName = ad.getName();
			name = ad.getCnName();
			password=ad.getPassword();
			security=ad.getControllerSecurity();
			code = ad.getValidateCode();
			p.put("userName",p.get("userName")==null?userName:(p.get("userName")+","+userName));
			p.put("name",p.get("name")==null?name:(p.get("name")+","+name));
			p.put("code",p.get("code")==null?name:(p.get("code")+","+code));
			p.put("password",p.get("password")==null?("("+password+")"):(p.get("password")+",("+password)+")");
			if(security == null || security.size() == 0){
				p.put("security",p.get("security")==null?("(*)"):(p.get("security")+";(*)"));
			}else{
				String add = (String) p.get("security");
				add += ";(";
				boolean b = false;
				for(String str:security){
					if(b){
						add+=",";
					}
					add+=str;
					b=true;
				}
				add+=")";
				p.put("security",add);
			}
		}
		SystemConfigUtil.saveConfig("admin",p);
	}
	
	public static Admin checkUser(String userName, String password, String validateCode) throws Exception{
		Admin admin = securityMap.get(userName);
		if (admin != null) {
			if (Hash.getHash(admin.getPassword(), "MD5").equals(password)) {
				if (StringUtils.isBlank(validateCode)) {
					return admin;
				} else {
					if (admin.getValidateCode().equals(validateCode)) {
						return admin;
					}
				}
			}
		}
		return null;
	}

	public static Admin getAdminByUser(Sys_user user){
		return securityMap.get(user.getName());
	}

	public static Sys_user adminToUser(Admin admin) {
		if (admin == null) {
			return null;
		}
		Sys_user user = new Sys_user();
		user.setName(admin.getName());
		user.setRealName(admin.getCnName());
		user.setLoginTime(new Date());
		return user;
	}

}
