<<<<<<< HEAD
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<!--[if gt IE 9]>
<script src="${ctx}/jslib/jquery/jquery.placeholder.min.js"></script>
<![endif]-->
<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery-1.9.1.js"></script>
<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery.ellipsis.min.js"></script>
<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery-migrate-1.2.1.min.js"></script>
<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery.cookie.js"></script>
<script src="<%=request.getContextPath() %>/resource/js/serialize-object.js"></script>
<link href="<%=request.getContextPath() %>/resource/css/btn.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath() %>/resource/css/common.css" rel="stylesheet" type="text/css"/>

<%
	String groups = request.getParameter("groups");
	String[] groupArray = groups.split(",");
	List groupList = Arrays.asList(groupArray);
	//if(groupList.contains("sigma")) {
%>
	<%-- <link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/jslib/sigmagrid/grid/calendar/calendar-blue.css"  />
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/calendar/calendar.js"></script>
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/calendar/calendar-cn-utf8.js"></script>
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/calendar/calendar-setup.js"></script>
	
	<link rel="stylesheet" type="text/css" href="${ctx}/jslib/sigmagrid/grid/gt_grid.css" />
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/gt_msg_cn.js"></script>
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/gt_grid_all.js"></script>
	
	<script src="<%= request.getContextPath() %>/jslib/jccp/jccp_sigma_extends.js"></script>
	<script src="<%= request.getContextPath() %>/jslib/jccp/jccp_sigma_export.js"></script>
	<script src="${ctx}/jslib/jccp/jccp_sigma_column_definer.js"></script> --%>
<%	
	//}
	if(groupList.contains("jqGrid")) {
%>
	<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath() %>/resource/jslib/jqGrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-ui/themes/redmond/jquery-ui-1.10.3.custom.css" />
	<script src="<%=request.getContextPath() %>/resource/jslib/jqGrid/js/i18n/grid.locale-cn.js"></script>
	<script src="<%=request.getContextPath() %>/resource/jslib/jqGrid/js/jquery.jqGrid.min.js"></script>
	<script src="<%=request.getContextPath() %>/resource/js/jqgrid-support.js"></script>
<%
	}
	if(groupList.contains("my97DatePicker")) {
		//使用方法：class="Wdate" onClick="WdatePicker({dateFmt:'yyyy',onpicked:function(dq){reloadInfo();}})"
%>
	<script src="<%=request.getContextPath() %>/resource/jslib/my97DatePicker/WdatePicker.js"></script>
<%
	}
	if(groupList.contains("jquery-validate")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-validate/css/validator.css" />
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-validate/jquery.validate.js"></script>
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-validate/localization/messages_cn.js"></script>
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-validate/jccp_validate.js"></script>
<%
	}
	if(groupList.contains("aofa-validate")) {
%>
	<script src="<%=request.getContextPath() %>/resource/js/form_validate.js"></script>
	
<%
	}
	if(groupList.contains("echart")) {
%>
	<script src="<%=request.getContextPath() %>/resource/jslib/echarts/echarts.js"></script>
	
<%
	}
	if(groupList.contains("jquery-ui")) {
%>
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-ui/jquery-ui.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-ui/jquery-ui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-ui/themes/redmond/jquery-ui-1.10.3.custom.css" />
<%
	}
	if(groupList.contains("jquery-ztree")) {
%>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/jquery-ztree/css/zTreeStyle/zTreeStyle.css" type="text/css" />
	<script language="javascript" src="<%=request.getContextPath() %>/resource/jslib/jquery-ztree/js/jquery.ztree.all.min.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/resource/js/ztree-support.js"></script>
<%
	}
	if(groupList.contains("jquery-layout")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-layout/layout-default.css" />
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-layout/jquery.layout.js"></script>
<%	}
	if(groupList.contains("artDialog")) {
%>
	<script src="<%=request.getContextPath() %>/resource/jslib/artDialog/jquery.artDialog.source.js?skin=gray"></script>
	<script src="<%=request.getContextPath() %>/resource/jslib/artDialog/plugins/iframeTools.source.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/artDialog/skins/gray.css" />
	<script src="<%=request.getContextPath() %>/resource/js/art-dialog-support.js"></script>
<%
	}
	if(groupList.contains("easyui")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/easyui/themes/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/easyui/themes/icon.css">
	<link href="<%=request.getContextPath() %>/resource/jslib/easyui/themes/color.css"/>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resource/jslib/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resource/jslib/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/jslib/easyui/plugins/jquery.treegrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/easyui-table-refresh-support.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/jqgrid-convert-easyui.js"></script>

<%
	}
	if(groupList.contains("easyui-ps")){
	%>
		<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/easyui-dialog-support.js"></script>
	<%
	}
	if(groupList.contains("easyui-cs")){
	%>
		<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/easyui-dialog-top-widow-support.js"></script>
	<%
	}
	if(groupList.contains("uploadFile")){
%>
	<link href="<%=request.getContextPath() %>/resource/jslib/jquery-uploadFile/css/custom.css" rel="stylesheet">
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-uploadFile/js/jquery.uploadfile.js"></script>
<%}
	if(groupList.contains("menu-item-style")){
%>
	<link href="<%=request.getContextPath() %>/resource/css/menu-item.css" rel="stylesheet" type="text/css"/>
<%
	}
	if(groupList.contains("bootstrap")){
		
	%>
		<!-- 新 Bootstrap 核心 CSS 文件 -->
		<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/bootstrap/css/bootstrap.min.css">
		
		<!-- 可选的Bootstrap主题文件（一般不用引入） -->
		<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/css/bootstrap-theme.min.css">
		
		<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
		<script src="<%=request.getContextPath() %>/resource/jslib/bootstrap/js/bootstrap.min.js"></script>
	<%
	}
	if(groupList.contains("gaodeMap")){
	%>
	<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
	<script src="http://webapi.amap.com/maps?v=1.3&key=ed718147ef745b97bb5c995716359fdb&plugin=AMap.Geocoder"></script>
	<%
	}
	//if(groupList.contains("highcharts")) {
	if(groupList.contains("handleView")){
	%>
	<script src="<%=request.getContextPath() %>/resource/js/handle_view.js"></script>
	<%
	}
	//if(groupList.contains("highcharts")) {
	%>
	<%
	//if(groupList.contains("highcharts")) {
	if(groupList.contains("jquery-rotate")){
	%>
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery-rotate.js"></script>
	<%
	}
	if(groupList.contains("message-box")){
		%>
		<script src="<%=request.getContextPath() %>/resource/js/message-content.js"></script>
		<%
	}
	if(groupList.contains("codemirror")){
		%>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/lib/codemirror.css"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/lib/codemirror.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/mode/xml/xml.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/model/css/css.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/model/htmlmixed/htmlmixed.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/model/javascript/javascript.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/model/properties/properties.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/addon/selection/active-line.js"></script>
	<%
	}
		if(groupList.contains("jquery-form")) {
	%>
		<script src="<%=request.getContextPath() %>/resource/jslib/jquery-form/jquery.form.js"></script>
		<script src="<%=request.getContextPath() %>/resource/js/ajax_form_support.js"></script>
	<%
	}
	//if(groupList.contains("highcharts")) {
	%>
<%-- 
	<script src="${ctx}/jslib/highcharts/highcharts.js"></script>
	<script src="${ctx}/jslib/highcharts/modules/exporting.js"></script>
	<script src="${ctx}/jslib/highcharts/themes/grid.js"></script>
	<script src="${ctx}/jslib/highcharts/jquery-highcharts.js"></script>
	<script src="${ctx}/jslib/highcharts/no-data-to-display.src.js"></script>
<%
	}
%>
	<script src="${ctx}/jslib/tipsy/jquery.tipsy.js"></script>
	<link rel="stylesheet" href="${ctx}/jslib/tipsy/tipsy.css">
	<link rel="stylesheet" href="${ctx}/jslib/tipsy/tipsy-docs.css">
	<script src="${ctx}/page/layout/003/content.js"></script>
    <link rel="stylesheet" href="${ctx}/page/layout/003/content.css" />
    <link rel="stylesheet" href="${ctx}/page/layout/003/button.css" />
    <link rel="stylesheet" href="${ctx}/page/layout/003/icon.css" /> --%>
<script src="<%=request.getContextPath() %>/resource/jslib/tipsy/jquery.tipsy.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/tipsy/tipsy.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/tipsy/tipsy-docs.css">
=======
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<!--[if gt IE 9]>
<script src="${ctx}/jslib/jquery/jquery.placeholder.min.js"></script>
<![endif]-->
<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery-1.9.1.js"></script>
<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery.ellipsis.min.js"></script>
<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery-migrate-1.2.1.min.js"></script>
<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery.cookie.js"></script>
<script src="<%=request.getContextPath() %>/resource/js/serialize-object.js"></script>
<link href="<%=request.getContextPath() %>/resource/css/btn.css" rel="stylesheet" type="text/css"/>
<link href="<%=request.getContextPath() %>/resource/css/common.css" rel="stylesheet" type="text/css"/>

<%
	String groups = request.getParameter("groups");
	String[] groupArray = groups.split(",");
	List groupList = Arrays.asList(groupArray);
	//if(groupList.contains("sigma")) {
%>
	<%-- <link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/jslib/sigmagrid/grid/calendar/calendar-blue.css"  />
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/calendar/calendar.js"></script>
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/calendar/calendar-cn-utf8.js"></script>
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/calendar/calendar-setup.js"></script>
	
	<link rel="stylesheet" type="text/css" href="${ctx}/jslib/sigmagrid/grid/gt_grid.css" />
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/gt_msg_cn.js"></script>
	<script src="<%= request.getContextPath() %>/jslib/sigmagrid/grid/gt_grid_all.js"></script>
	
	<script src="<%= request.getContextPath() %>/jslib/jccp/jccp_sigma_extends.js"></script>
	<script src="<%= request.getContextPath() %>/jslib/jccp/jccp_sigma_export.js"></script>
	<script src="${ctx}/jslib/jccp/jccp_sigma_column_definer.js"></script> --%>
<%	
	//}
	if(groupList.contains("jqGrid")) {
%>
	<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath() %>/resource/jslib/jqGrid/css/ui.jqgrid.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-ui/themes/redmond/jquery-ui-1.10.3.custom.css" />
	<script src="<%=request.getContextPath() %>/resource/jslib/jqGrid/js/i18n/grid.locale-cn.js"></script>
	<script src="<%=request.getContextPath() %>/resource/jslib/jqGrid/js/jquery.jqGrid.min.js"></script>
	<script src="<%=request.getContextPath() %>/resource/js/jqgrid-support.js"></script>
<%
	}
	if(groupList.contains("my97DatePicker")) {
		//使用方法：class="Wdate" onClick="WdatePicker({dateFmt:'yyyy',onpicked:function(dq){reloadInfo();}})"
%>
	<script src="<%=request.getContextPath() %>/resource/jslib/my97DatePicker/WdatePicker.js"></script>
<%
	}
	if(groupList.contains("jquery-validate")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-validate/css/validator.css" />
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-validate/jquery.validate.js"></script>
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-validate/localization/messages_cn.js"></script>
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-validate/jccp_validate.js"></script>
<%
	}
	if(groupList.contains("aofa-validate")) {
%>
	<script src="<%=request.getContextPath() %>/resource/js/form_validate.js"></script>
	
<%
	}
	if(groupList.contains("echart")) {
%>
	<script src="<%=request.getContextPath() %>/resource/jslib/echarts/echarts.js"></script>
	
<%
	}
	if(groupList.contains("jquery-ui")) {
%>
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-ui/jquery-ui.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-ui/jquery-ui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-ui/themes/redmond/jquery-ui-1.10.3.custom.css" />
<%
	}
	if(groupList.contains("jquery-ztree")) {
%>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/jquery-ztree/css/zTreeStyle/zTreeStyle.css" type="text/css" />
	<script language="javascript" src="<%=request.getContextPath() %>/resource/jslib/jquery-ztree/js/jquery.ztree.all.min.js"></script>
	<script language="javascript" src="<%=request.getContextPath() %>/resource/js/ztree-support.js"></script>
<%
	}
	if(groupList.contains("jquery-layout")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/jquery-layout/layout-default.css" />
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-layout/jquery.layout.js"></script>
<%	}
	if(groupList.contains("artDialog")) {
%>
	<script src="<%=request.getContextPath() %>/resource/jslib/artDialog/jquery.artDialog.source.js?skin=gray"></script>
	<script src="<%=request.getContextPath() %>/resource/jslib/artDialog/plugins/iframeTools.source.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/artDialog/skins/gray.css" />
	<script src="<%=request.getContextPath() %>/resource/js/art-dialog-support.js"></script>
<%
	}
	if(groupList.contains("easyui")) {
%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/easyui/themes/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jslib/easyui/themes/icon.css">
	<link href="<%=request.getContextPath() %>/resource/jslib/easyui/themes/color.css"/>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resource/jslib/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/resource/jslib/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/jslib/easyui/plugins/jquery.treegrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/easyui-table-refresh-support.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/jqgrid-convert-easyui.js"></script>

<%
	}
	if(groupList.contains("easyui-ps")){
	%>
		<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/easyui-dialog-support.js"></script>
	<%
	}
	if(groupList.contains("easyui-cs")){
	%>
		<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/easyui-dialog-top-widow-support.js"></script>
	<%
	}
	if(groupList.contains("uploadFile")){
%>
	<link href="<%=request.getContextPath() %>/resource/jslib/jquery-uploadFile/css/custom.css" rel="stylesheet">
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery-uploadFile/js/jquery.uploadfile.js"></script>
<%}
	if(groupList.contains("menu-item-style")){
%>
	<link href="<%=request.getContextPath() %>/resource/css/menu-item.css" rel="stylesheet" type="text/css"/>
<%
	}
	if(groupList.contains("bootstrap")){
		
	%>
		<!-- 新 Bootstrap 核心 CSS 文件 -->
		<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/bootstrap/css/bootstrap.min.css">
		
		<!-- 可选的Bootstrap主题文件（一般不用引入） -->
		<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/css/bootstrap-theme.min.css">
		
		<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
		<script src="<%=request.getContextPath() %>/resource/jslib/bootstrap/js/bootstrap.min.js"></script>
	<%
	}
	if(groupList.contains("gaodeMap")){
	%>
	<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
	<script src="http://webapi.amap.com/maps?v=1.3&key=ed718147ef745b97bb5c995716359fdb&plugin=AMap.Geocoder"></script>
	<%
	}
	//if(groupList.contains("highcharts")) {
	if(groupList.contains("handleView")){
	%>
	<script src="<%=request.getContextPath() %>/resource/js/handle_view.js"></script>
	<%
	}
	//if(groupList.contains("highcharts")) {
	%>
	<%
	//if(groupList.contains("highcharts")) {
	if(groupList.contains("jquery-rotate")){
	%>
	<script src="<%=request.getContextPath() %>/resource/jslib/jquery/jquery-rotate.js"></script>
	<%
	}
	if(groupList.contains("message-box")){
		%>
		<script src="<%=request.getContextPath() %>/resource/js/message-content.js"></script>
		<%
	}
	if(groupList.contains("codemirror")){
		%>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/lib/codemirror.css"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/lib/codemirror.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/mode/xml/xml.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/model/css/css.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/model/htmlmixed/htmlmixed.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/model/javascript/javascript.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/model/properties/properties.js"></script>
		<script src="<%=request.getContextPath() %>/resource/jslib/codemirror-5.22.0/addon/selection/active-line.js"></script>
	<%
	}
		if(groupList.contains("jquery-form")) {
	%>
		<script src="<%=request.getContextPath() %>/resource/jslib/jquery-form/jquery.form.js"></script>
		<script src="<%=request.getContextPath() %>/resource/js/ajax_form_support.js"></script>
	<%
	}
	//if(groupList.contains("highcharts")) {
	%>
<%-- 
	<script src="${ctx}/jslib/highcharts/highcharts.js"></script>
	<script src="${ctx}/jslib/highcharts/modules/exporting.js"></script>
	<script src="${ctx}/jslib/highcharts/themes/grid.js"></script>
	<script src="${ctx}/jslib/highcharts/jquery-highcharts.js"></script>
	<script src="${ctx}/jslib/highcharts/no-data-to-display.src.js"></script>
<%
	}
%>
	<script src="${ctx}/jslib/tipsy/jquery.tipsy.js"></script>
	<link rel="stylesheet" href="${ctx}/jslib/tipsy/tipsy.css">
	<link rel="stylesheet" href="${ctx}/jslib/tipsy/tipsy-docs.css">
	<script src="${ctx}/page/layout/003/content.js"></script>
    <link rel="stylesheet" href="${ctx}/page/layout/003/content.css" />
    <link rel="stylesheet" href="${ctx}/page/layout/003/button.css" />
    <link rel="stylesheet" href="${ctx}/page/layout/003/icon.css" /> --%>
<script src="<%=request.getContextPath() %>/resource/jslib/tipsy/jquery.tipsy.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/tipsy/tipsy.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jslib/tipsy/tipsy-docs.css">
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
<script src="<%=request.getContextPath() %>/resource/js/ajax_support.js"></script>