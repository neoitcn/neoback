<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
		<base href="<%=basePath%>">
		<title>ffff</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
   
   <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
</head>

<script type="text/javascript">
	function picname(){
		 var spans = document.getElementById("picture").getElementsByTagName("img");
	       
		if (spans.length > 2) {
			for (i = 0; i < spans.length; i++) {
				var result = spans.item(i).alt;
                /* alert(i+":"+spans.item(i).alt); */
				$("#info").append('保存的图片名称:<input type="text" name="pic" value="'+result+'"/><br>');

			}
		}
		
	}
</script>
<body>
    欢迎来到启芯教育1。。。。。。
    内容是1: ${content}
  上传图片信息1: ${msg } 
    
    

    
  
</body>
</html> 