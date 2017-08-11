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

 <!--   引入js jar包 -->
 <script type="text/javascript" src="../js/jquery-1.7.2.min.js"></script>
</head>
<script type="text/javascript">
 /* 获取ueditor编辑器内容中img标签中图片的名称(alt的值), 保存到 <input/>中 */
	function picname(){
		var ue = UE.getEditor('container'); 
		var str=ue.getPlainTxt();//s就是编辑器的带格式的内容
		alert("编辑器内容是:"+str);
		
		//var str='<img src="1502274370754037759.png" title="1502274370754037759.png" alt="66.png"/><br>HAHAH<img src="1502274370754037759.png" title="1502274370754037759.png" alt="777.png"/><br>';
		//匹配图片（g表示匹配所有结果i表示区分大小写）
		var imgReg = /<img.*?(?:>|\/>)/gi;
		//匹配src属性
		var srcReg = /title=[\'\"]?([^\'\"]*)[\'\"]?/i;
		var arr = str.match(imgReg);
		alert('所有已成功匹配图片的数组：'+arr);
		if(arr!=null){
		for (var i = 0; i < arr.length; i++) {
		 var src = arr[i].match(srcReg);
		 //获取图片地址
		 if(src[1]){
		  alert('已匹配的图片地址'+(i+1)+'：'+src[1]);
		 $("#savepic").append('保存的图片名称:<input type="text" name="pic" value="'+src[1]+'"/><br>');
				
		 }
		}}
		document.forms.submit();
	}
</script>
<body>

  <div  style="margin:auto;width:1025px;">
	<form action="../admin/uploadNews.do"  name="forms" method="post"  enctype="multipart/form-data">
		<!--此div保存新闻中的图片的名字 -->
        <div id="savepic"></div>
		
		
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
		  请输入发布者id:<input type="text"  name="createId" /><br>
		
		<!-- 加载编辑器的容器 -->
		 <script id="container" name="content" type="text/plain" style="width:1024px;height:450px;">
                        此处输入内容...
        </script>
        	
     
		<!-- <input type="button"   onclick="picname()" value="提交" /> -->
	   <a href="javascript:picname()" style="text-decoration: none;">提交</a>
	   
	</form>
  </div>
	<!-- 实例化编辑器 -->
	<script type="text/javascript">
	        var ue = UE.getEditor('container'); 
	</script>
</body>
</html>