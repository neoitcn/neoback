$(function(){
	$(".view input[type='text']").each(function(i,v){
		$(v).attr("readonly","readonly");
		$(v).removeAttr("onclick");
		$(v).unbind();
	});
	$(".view input[type='password']").each(function(i,v){
		$(v).attr("readonly","readonly");
		$(v).removeAttr("onclick");
		$(v).unbind();
	});
	$(".view input[type='checkbox']").each(function(i,v){
		$(v).attr("disabled","disabled");
		$(v).removeAttr("onclick");
		$(v).unbind();
	});
	$(".view input[type='radio']").each(function(i,v){
		$(v).attr("disabled","disabled");
		$(v).removeAttr("onclick");
		$(v).unbind();
	});
	$(".view textarea").each(function(i,v){
		$(v).attr("readonly","readonly");
		$(v).removeAttr("onclick");
		$(v).unbind();
	});
	$(".view select").each(function(i,v){
		$(v).attr("disabled","disabled");
		$(v).removeAttr("onclick");
		$(v).unbind();
	});
})