package com.neo.sys.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.neo.sys.emue.SessionAttribute;

/**
 * 验证码生成工具类
 *
 */
public class ValidateCodeImageUtil {

	private static DefaultKaptcha dk = new DefaultKaptcha();
	
	static{
		try {
			Properties p = SystemConfigUtil.getConfigMap("kaptcha");
			Config cfg = new Config(p);
			dk.setConfig(cfg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 图片生成格式，支持gif，png和jpg
	 */
	private static String formatType = "jpg";
	
	private static int randomSize = 4;
	
	
	/**
	 * 生成一个验证码图片，
	 * @content 指定验证码内容
	 * @param realPath 验证码保存的真实位置
	 * 返回content便于链式调用
	 */
	public static String createImageValidateCode(String content,String realPath) throws Exception{
		BufferedImage bi = dk.createImage(content);
		ImageIO.write(bi, formatType, new File(realPath));
		return content;
	}
	
	/**
	 * 生成一个随机生成的数字验证码，长度为4位
	 * @param realPath 指定验证码保存的真实位置
	 * 返回生成的验证码文字
	 */
	public static String createNumImageValidateCode(String realPath) throws Exception{
		String code = "";
		for(int i=0;i<randomSize;i++){
			code+=new Random().nextInt(10);
		}
		return createImageValidateCode(code,realPath);
	}
	
	/**
	 * 创建并将验证码的信息保存到session中
	 * @param content
	 * @param realPath
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static String createAndPutInSession(String content,String realPath,HttpSession session) throws Exception{
		createImageValidateCode(content,realPath);
		session.setAttribute(SessionAttribute.VALIDATE_CODE, content);
		session.setAttribute(SessionAttribute.VALIDATE_CODE_ADDRESS, realPath);
		return content;
	}
	
	/**
	 * 创建随机验证码并将验证码信息保存到session中
	 * @param content
	 * @param realPath
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static String createRandomAndPutInSession(String realPath,HttpSession session) throws Exception{
		String content = createNumImageValidateCode(realPath);
		session.setAttribute(SessionAttribute.VALIDATE_CODE, content);
		session.setAttribute(SessionAttribute.VALIDATE_CODE_ADDRESS, realPath);
		session.setAttribute(SessionAttribute.ISNEED_VALIDATE_CODE, SessionAttribute.need_validate_code);
		return content;
	}
	
	/**
	 * 创建一个验证码并输出
	 * @param session
	 * @param os
	 * @return
	 * @throws Exception
	 */
	public static String createCodePutInSessionAndOutput(HttpSession session,String context,OutputStream os) throws Exception{
		session.setAttribute(SessionAttribute.VALIDATE_CODE, context);
		session.setAttribute(SessionAttribute.ISNEED_VALIDATE_CODE, SessionAttribute.need_validate_code);
		BufferedImage bi = dk.createImage(context);
		ImageIO.write(bi, formatType, os);
		return context;
	}
	/**
	 * 创建一个随机验证码并输出
	 * @param session
	 * @param os  
	 * @return
	 * @throws Exception
	 */
	public static String createRandomPutInSessionAndOutput(HttpSession session,OutputStream os) throws Exception{
		String code = "";
		for(int i=0;i<randomSize;i++){
			code+=new Random().nextInt(10);
		}
		session.setAttribute(SessionAttribute.VALIDATE_CODE, code);
		session.setAttribute(SessionAttribute.ISNEED_VALIDATE_CODE, SessionAttribute.need_validate_code);
		BufferedImage bi = dk.createImage(code);
		ImageIO.write(bi, formatType, os);
		return code;
	}
	
	public static void clearSessionValidateCodeInfo(HttpSession session){
		String address = (String) session.getAttribute(SessionAttribute.VALIDATE_CODE_ADDRESS);
		String sessionId = session.getId();
		session.removeAttribute(SessionAttribute.VALIDATE_CODE);
		session.removeAttribute(SessionAttribute.VALIDATE_CODE_ADDRESS);
		session.removeAttribute(SessionAttribute.ISNEED_VALIDATE_CODE);
		if(StringUtils.isNotBlank(address)){
			File f = new File(address);
			int index = 0;
			while(f.exists()){
				f.delete();
				if(f.exists()){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
					if(index > 5){
						System.out.println("清理验证码图片文件失败，sessionID是："+sessionId);
						break;
					}
				}
				index++;
			}
		}
	}
	
	public static String getFormatType(){
		return formatType;
	}
	
}
