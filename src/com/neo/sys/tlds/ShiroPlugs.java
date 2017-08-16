package com.neo.sys.tlds;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *自定义jsp标签，对shiro标签的permission进行扩展。
 */
public class ShiroPlugs extends SimpleTagSupport {

	private static final long serialVersionUID = -8523271694414595148L;

	private String name;
	private String permissions;
	private String a_add_attr;
	private String u_add_attr;
	private String display;
	private String style;
	private String id;
	private String width;
	private String height;
	private String className;
	private String readonly;
	private String onblur;
	private String onchange;
	private String onclick;
	private String ondblclick;
	private String onfocus;
	private String onkeydown;
	private String onkeypress;
	private String onkeyup;
	private String onmousedown;
	private String onmousemove;
	private String onmouseout;
	private String onmouseover;
	private String onmouseup;

	public void setName(String name) {
		this.name = name;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public void setA_add_attr(String a_add_attr) {
		this.a_add_attr = a_add_attr;
	}

	public void setU_add_attr(String u_add_attr) {
		this.u_add_attr = u_add_attr;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public void setOnfocus(String onfocus) {
		this.onfocus = onfocus;
	}

	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}

	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}

	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().println(createTag());
		super.doTag();
	}
	
	private String createTag(){
		
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(name).append(" ");//组装标签名
		
		String []acceptAddAttr = null;
		String []unAcceptAddAttr = null;
		
		//如果权限不通过，则不予以显示
		boolean isAccept = checkPermission();
		
		if(!isAccept)
			if("false".equals(display)){
				return "";
			}
		
		if(StringUtils.isNotBlank(a_add_attr)){
			acceptAddAttr = a_add_attr.trim().split("[|]");
		}
		if(StringUtils.isNotBlank(u_add_attr)){
			unAcceptAddAttr = u_add_attr.trim().split("[|]");
		}
		
		if(isAccept){
			if(acceptAddAttr!=null){
				for(String str:acceptAddAttr){
					sb.append(" ").append(str).append(" ");
				}
			}
		}else{
			if(unAcceptAddAttr!=null){
				for(String str:unAcceptAddAttr){
					sb.append(" ").append(str).append(" ");
				}
			}
		}
		
		if(id != null)
			sb.append("id=\"").append(id).append("\" ");//组装标签属性
		if(style != null)
			sb.append("style=\"").append(style).append("\" ");//组装标签属性
		if(width != null)
			sb.append("width=\"").append(width).append("\" ");//组装标签属性
		if(height != null)
			sb.append("height=\"").append(height).append("\" ");//组装标签属性
		if(className != null)
			sb.append("class=\"").append(className).append("\" ");//组装标签属性
		if(readonly != null)
			sb.append("readonly=\"").append(readonly).append("\" ");//组装标签属性
		if(onblur != null)
			sb.append("onblur=\"").append(onblur).append("\" ");//组装标签属性
		if(onchange != null)
			sb.append("onchange=\"").append(onchange).append("\" ");//组装标签属性
		if(onclick != null)
			sb.append("onclick=\"").append(onclick).append("\" ");//组装标签属性
		if(ondblclick != null)
			sb.append("ondblclick=\"").append(ondblclick).append("\" ");//组装标签属性
		if(onfocus != null)
			sb.append("onfocus=\"").append(onfocus).append("\" ");//组装标签属性
		if(onkeydown != null)
			sb.append("onkeydown=\"").append(onkeydown).append("\" ");//组装标签属性
		if(onkeypress != null)
			sb.append("onkeypress=\"").append(onkeypress).append("\" ");//组装标签属性
		if(onkeyup != null)
			sb.append("onkeyup=\"").append(onkeyup).append("\" ");//组装标签属性
		if(onmousedown != null)
			sb.append("onmousedown=\"").append(onmousedown).append("\" ");//组装标签属性
		if(onmousemove != null)
			sb.append("onmousemove=\"").append(onmousemove).append("\" ");//组装标签属性
		if(onmouseout != null)
			sb.append("onmouseout=\"").append(onmouseout).append("\" ");//组装标签属性
		if(onmouseover != null)
			sb.append("onmouseover=\"").append(onmouseover).append("\" ");//组装标签属性
		if(onmouseup != null)
			sb.append("onmouseup=\"").append(onmouseup).append("\" ");//组装标签属性
		sb.append(">");
		StringWriter sw = new StringWriter();
		try {
			getJspBody().invoke(sw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//解析标签体中是否存在accept这样的标签
		
		String body = getAcceptTagConent(sw.toString(),isAccept);
		
		if(StringUtils.isBlank(body)){
			sb.append(sw.toString());
		}else{
			sb.append(body);
		}
		
		sb.append("</").append(name).append(">");
		return sb.toString();
	}
	
	/**
	 * 解析权限描述符，返回and和or描述的条件集合。
	 * {or=[admin:r, admin:w, user:r], and=[user:r]}
	 * @param content
	 * @return
	 */
	private Map<String,List<String>> getPermissionsDirection(String content){
		if(content == null){
			return null;
		}
		Map<String,List<String>> map = new HashMap<>();
		if(content.contains(" and ") || content.contains(" or ")){
			Pattern p = Pattern.compile("( and )|( or )");
			Matcher m = p.matcher(content);
			int start = 0;
			List<String> andList = new ArrayList<>();
			List<String> orList = new ArrayList<>();
			String tempStr; //识别and或or的字符串
			String rStr=null;//临时存入关系描述字符串
			boolean isFirst = true; //标识第一个是否为第一次。第一次存入到or中。
			while(m.find()){
				tempStr = content.substring(m.start(),m.end());
				if(isFirst){
					isFirst = false;
					rStr = tempStr.contains("and")?"and":"or";
				}
				if("and".equals(rStr)){
					andList.add(content.substring(start,m.start()).trim());
				}else{
					orList.add(content.substring(start,m.start()).trim());
				}
				rStr = tempStr.contains("and")?"and":"or";
				start = m.end();
			}
			if("and".equals(rStr)){
				andList.add(content.substring(start).trim());
			}else{
				orList.add(content.substring(start).trim());
			}
			map.put("and",andList);
			map.put("or",orList);
		}else{
			List<String> list = new ArrayList<>();
			list.add(content.trim());
			map.put("and",list);
		}
		return map;
	}

	/**
	 * 检查当前用户是否具备所描述的权限
	 * @param content
	 * @return
	 */
	private boolean checkPermission(){
		Map<String,List<String>> permissionsMap = getPermissionsDirection(permissions);
		Subject subject = SecurityUtils.getSubject();
		boolean b = false;
		if(permissionsMap!=null){
			List<String> andList = permissionsMap.get("and");
			List<String> orList = permissionsMap.get("or");
			if(andList!=null && andList.size()>0){
				b = subject.isPermittedAll(andList.toArray(new String[]{}));
			}
			//如果and条件通过，则or条件无需检查。
			if(!b){
				if(orList != null && orList.size() > 0){
					for(String str:orList){
						 b = subject.isPermitted(str);
						 if(b){
							 break;
						 }
					}
				}
			}
		}
		return b;
	}
	
	/**
	 * 将accept和unaccept中的内容分离
	 * @param content
	 * @return
	 */
	private String getAcceptTagConent(String content,boolean isAccept){
		if(content == null){
			return "";
		}
		if(isAccept){
			Pattern p = Pattern.compile("<( *)accept( *)>");
			Matcher m = p.matcher(content);  
			if(m.find()){
				String aStr = content.substring(m.end());
				p = Pattern.compile("</( *)accept>( *)");
				m = p.matcher(aStr);
				if(m.find()){
					aStr = aStr.substring(0,m.start());
					return aStr;
				}else{
					return "";
				}
			}
		}else{
			Pattern p = Pattern.compile("<( *)unaccept( *)>");
			Matcher m = p.matcher(content);  
			if(m.find()){
				String aStr = content.substring(m.end());
				p = Pattern.compile("</( *)unaccept( *)>");
				m = p.matcher(aStr);
				if(m.find()){
					aStr = aStr.substring(0,m.start());
					return aStr;
				}else{
					return "";
				}
			}
		}
		return "";
	}
	
}
