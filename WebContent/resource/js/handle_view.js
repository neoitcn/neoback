<<<<<<< HEAD
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
=======
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
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
})