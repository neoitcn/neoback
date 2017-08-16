package com.neo.sys.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpSession;

import com.neo.entity.Sys_user;
import com.neo.sys.emue.SessionAttribute;

/**
 * 检查用户是否在多个地方登陆
 * 
 *
 *         该类中u_sessions中保存的用户是同一个账号最后一次登录的session。
 *         id_sessions保正了所有的已成功登陆的session。
 *
 *
 */
public class SessionManagerUtil {

	private static List<SessionResult> listSessions = new LinkedList<>();

	private static final ReentrantLock lock = new ReentrantLock();

	/**
	 * 保存session对象 session中必须拥有user对象
	 * 
	 * @param session
	 */
	public static void putSession(HttpSession session) {
		final ReentrantLock inLock = lock;
		inLock.lock();
		try {

			if (session == null) {
				throw new NullPointerException("session不能为空");
			}

			Sys_user user = (Sys_user) session.getAttribute(SessionAttribute.CURRENT_USER);

			if (user == null) {
				throw new Exception("user不存在");
			}

			listSessions.add(new SessionResult(user.getName(), session.getId(), session));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inLock.unlock();
		}
	}

	/**
	 * 根据用户获取ID获取session
	 * 
	 * 此方法不保证当前的session一定可用
	 * 
	 * @param userId
	 * @return
	 */
	public static List<HttpSession> getSessionByUserId(String loginName) {
		List<HttpSession> sessions = new ArrayList<>();
		for (SessionResult hs : listSessions) {
			if (hs.getUserId().equals(loginName)) {
				sessions.add(hs.getSession());
			}
		}
		return sessions;
	}

	/**
	 * 根据sessionId获取session
	 */
	public static HttpSession getSessionBySessionId(String sessionId) {
		for (SessionResult hs : listSessions) {
			if (hs.getSessionId().equals(sessionId)) {
				return hs.getSession();
			}
		}
		return null;
	}

	/**
	 * 只是从列表中移除session，但不执行销毁session操作，一般用在SessionListener的销毁方法中。
	 * 
	 * @param userId
	 */
	public static void deleteSessionByUserId(String sessionId) {
		final ReentrantLock inLock = lock;
		inLock.lock();
		try {
			for (int i = 0; i < listSessions.size(); i++) {
				SessionResult sr = listSessions.get(i);
				if (sr.getSessionId().equals(sessionId)) {
					listSessions.remove(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inLock.unlock();
		}
	}

	/**
	 * 根据用户ID销毁session 只销毁最后一次同一账号登陆的session。 该方法禁止在sessionListener中使用
	 * 
	 * @param userId
	 */
	public static void invalidSessionByUserId(String loginName) {
		final ReentrantLock inLock = lock;
		inLock.lock();
		try {
			for (int i = listSessions.size() - 1; i >= 0; i--) {
				SessionResult sr = listSessions.get(i);
				if (sr.getUserId().equals(loginName)) {
					sr.getSession().invalidate();
					listSessions.remove(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inLock.unlock();
		}
	}

	/**
	 * 根据用户ID销毁所有的session 该方法禁止在sessionListener中使用
	 * 
	 * @param userId
	 */
	public static void invalidSessionsByUserId(String loginName) {
		final ReentrantLock inLock = lock;
		inLock.lock();
		try {
			for (int i = 0; i < listSessions.size();) {
				SessionResult sr = listSessions.get(i);
				if (sr.getUserId().equals(loginName)) {
					sr.getSession().invalidate();
					listSessions.remove(i);
					continue;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			inLock.unlock();
		}
	}


	/**
	 * 检查用户是否在线
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean checkUserIsOnline(String loginName) {
		for (int i = 0; i < listSessions.size();i++) {
			SessionResult sr = listSessions.get(i);
			if (sr.getUserId().equals(loginName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取该用户在几个终端登录
	 * 
	 * @param userId
	 * @param currentSession
	 * @return
	 */
	public static int checkUserIsAlreadyOnLine(String loginName) {
		int index = 0;
		for (int i = 0; i < listSessions.size();i++) {
			SessionResult sr = listSessions.get(i);
			if (sr.getUserId().equals(loginName)) {
				index++;
			}
		}
		return index;
	}

}

class SessionResult {

	private String userId;
	private String sessionId;
	private HttpSession session;
	private Map<String,Object> extParam = new HashMap<String,Object>();

	public SessionResult(String userId, String sessionId, HttpSession session) {
		super();
		this.userId = userId;
		this.sessionId = sessionId;
		this.session = session;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	public Map<String,Object> get(){
		return extParam;
	}

}
