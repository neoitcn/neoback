<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
		 <base href="<%=basePath%>">
		<title>启芯教育</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
</head>
<body>
  <div style="margin-left:300px;margin-top: 200px;">
    <form action="admin/addNews.do" method="post"   enctype="multipart/form-data"> 
		  请输入新闻标题:<br> <textarea name="title" cols="40" rows="5">在这里输入内容...</textarea><br> 
		  请输入作者:<input TYPE="text" name="author" /><br>
		 请输入新闻分类(数字):<input TYPE="text" name="type" /><br>
		 请输入新闻等级:<input TYPE="text" name="level" /><br>
		 新闻链接地址:<input TYPE="text"
		  value="<% out.print(new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));%>.html" name="htmlUrl" /><br>
		  请输入新闻摘要:<br><textarea name="resume" cols="40" rows="5">在这里输入摘要...</textarea><br> 
		 请输入发布者:<input TYPE="text" name="creator" /><br>
		 <br>
		 <br>
	
		 请输入新闻内容:<br><textarea name="newscontent" cols="40" rows="5">在这里输入新闻内容...</textarea><br>       
		
		 请选择新闻图片:<input type="file" name="headimg" /><br>
		
		  请为上传递的新闻图片按顺序添加描述(每张描述结束后以"#"结尾):<br>
		<textarea name="picturedesc" cols="40" rows="5">在这里输入图片描述...</textarea><br> 
		  
		  
		  
		  
		  
		  
		  
		  
		  
		   <input TYPE="submit" value="点击发布"/><br>
  
  
    </form>
</div>

</body>
</html>

