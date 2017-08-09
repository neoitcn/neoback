<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<!-- 配置文件 -->
<script type="text/javascript" src="../ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="../ueditor/ueditor.all.js"></script>



</head>
<body>

  <div  style="margin:auto;width:1025px;">
	<form action="../admin/uploadNews.do" method="post"  enctype="multipart/form-data">
		<!-- 加载编辑器的容器 -->
		请输入新闻标题:<input type="text"  name="title" /><br><br>
		请上传新闻缩略图:<input type="file"  name="smallpicture" /><br><br>
		请输入新闻概要:<textarea rows="3" cols="50" name="resume"></textarea><br><br>
		请选择新闻类型:<select  id =  "sel" name="type" >
					 <option  value = "1" >头条新闻</option >
					 <option  value = "2" selected = "selected" >学院新闻</option >
					 </select ><br>
		 请选择显示级别:<select  id =  "sel" name="level" >
		 <option  value = "1"  selected = "selected">优先显示</option >
		 <option  value = "2" >普通显示</option >
		 </select ><br>
		 请输入文章作者:<input type="text"  name="author" /><br>
		  请输入发布者:<input type="text"  name="creator" /><br>
					 
	<script id="container" name="content" type="text/plain" style="width:1024px;height:450px;"></script>
		<input type="submit" value="提交" />
	</form>
  </div>


	<!-- 实例化编辑器 -->
	<script type="text/javascript">
	        var ue = UE.getEditor('container'); 
	</script>


</body>
</html>