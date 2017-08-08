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
		<title>ffff</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
</head>
<body>
	<div style="margin-left:400px;margin-top: 200px;">
	
		<form action="users/login.do" method="post"> 
		   <input TYPE="text" name="name" /><br>
		   <input TYPE="text" name="password" /><br>
		   <input TYPE="submit" value="登陆"/><br>
		  </form>
		 ${msg}
		 
	 </div>

</body>
</html>