<<<<<<< HEAD
//easyui dialog without html
//window所需的基本参数
//{width:窗口宽度，可以设为百分比,height:窗口高度，可以设为百分比,title:窗口标题,titleIco:窗口左上角的图标，依据easyui提供的api设置，比如需要一个空白图标，传入icon-blank。,url:要打开的页面}
//该方法用于打开一个内嵌页面，如果只是一个普通的窗口需要自定义内容，则无需如此。
//width，height，titleIcon可以省略。
var ContentWindow = function(data){
	data=data||{};
	this.width = data.width||window.innerWidth*0.9;
	//IE不支持startsWith和endsWith
	if(this.width.lastIndexOf&&this.width.lastIndexOf('%') == this.width.length-1){
		this.width = parseFloat(this.width)/100*window.innerWidth;
	}else{
		this.width = parseFloat(this.width);
	}
	this.height = data.height||window.innerHeight*0.9;
	if(this.height.lastIndexOf&&this.height.lastIndexOf('%') == this.height.length-1){
		this.height = parseInt(this.height)/100*window.innerHeight;
	}else{
		this.height = parseFloat(this.height);
	}
	this.title = data.title||'My Window';
//	this.titleIco = "iconCls:'"+(data.titleIco||"icon-blank")+"'";
	this.titleIco = data.titleIco||"icon-blank";
	this.id = new Date().getTime();
	if(data.url){
		if(data.url.indexOf('?')!=-1){
			this.url = data.url += '&' + this.id;
		}else{
			this.url = data.url += '?' + this.id;
		}
	}else{
		this.url = data.url;
	}
	this.closeBtn=typeof data.closeBtn==='boolean'?data.closeBtn:true;
	this.resize=data.resize;
	this.open=_openWindow;
	this.destory=destoryWindow;
	this.setTitle=setTitle;
	this.setWidth=setWidth;
	this.setHeight=setHeight;
	this.destoried = false;
	this.hasInit = false;
	this.buttons=[];
	this.beforeClose = data.beforeClose;
	if(data.okBtn){
		this.buttons.push({
            text: data.okBtn.text||'保存',
            iconCls: data.okBtn.iconCls||'icon-ok',
            handler: data.okBtn.handler||function(){
            	//默认调用子页面的save方法
            	if($("#"+this.id)[0].contentWindow.save)
            		$("#"+this.id)[0].contentWindow.save();
            }
        });
	}
	if(data.cancelBtn){
		this.buttons.push({
            text: data.cancelBtn.text||'关闭',
            iconCls: data.cancelBtn.iconCls||'icon-cancel',
            handler:data.cancelBtn.handler||function () {
            	obj.win.window('close');
            }
        });
	}
	this.data=data.data;
}

function initWindow(){
	/*$("body").append(
		"<div id='"+this.id+"' title='"+this.title+"' data-options=\""+this.titleIco+"\" style='width:"+this.width+";height:"+this.height+";display:none;'>" +
			"<iframe src='"+this.url+"' frameborder='no' width='100%' height='100%' marginwidth='0' marginheight='0' border='0' scrolling='no' allowtransparency='yes'>" +
			"</iframe>"+
		"</div>"
	);*/
	$("body").append(
		"<div id='"+this.id+"' style='position:relative;z-index:999999999;'>" +
			"<iframe frameborder='no' width='100%' height='100%' marginwidth='0' marginheight='0' border='0' allowtransparency='yes' style=' position:absolute;top:0;bottom:0;'>" +
			"</iframe>"+
			"<div data-role='q' style='background-color:rgba(255,255,255,0.5);position:absolute;top:0;bottom:0;left:0;right:0;'></div>"+
			"<div data-role='q' style='position:fixed;width:150px;height:20px;background-image:url(resource/img/ico/process2.gif);background-position:center;background-repeat:no-repeat;top:0;bottom:0;left:0;right:0;margin:auto;'></div>"+
		"</div>"
	);
}

//添加模糊效果：
function _addG(){
	return;
	var docum = $("body",window.top.document);
	for(var i=0;i<docum.children().length;i++){
		var d = $(docum.children()[i]);
		if(d.attr('id') == '_my97DP'){
			continue;
		}
		var num = d.attr('data-g-blur');
		d.attr('data-g-blur',isNaN(num)?1:Number(num)+1);
		d.addClass('g-blur');
	}

}

//移除模糊效果
function _removeG(){
	return;
	var docum = $("body",window.top.document);
	for(var i=0;i<docum.children().length;i++){
		var d = $(docum.children()[i]);
		if(d.attr('id') == '_my97DP'){
			continue;
		}
		var num = d.attr('data-g-blur');
		if(!isNaN(num)){
			if(num <= 1){
				d.removeClass('g-blur');
				d.removeAttr('data-g-blur');
			}else{
				d.attr('data-g-blur',--num);
			}
		}else{
			d.removeClass('g-blur');
			d.removeAttr('data-g-blur');
		}
	}
}

function _openWindow(){
	if(this.destoried){
		return;
	}
	if(!this.hasInit){
		_addG();
		initWindow.call(this);

		var arr = $("#"+this.id).find('div[data-role=q]');
		$(arr[1]).css('background-image','url(resource/img/ico/process2.gif)');
		$("#"+this.id).find('iframe').attr('src',this.url);
		$("#"+this.id).find('iframe').load(function(){
			arr.each(function(i,v){
				$(v).hide();
				switch(i){
					case 0:
						$(v).css('background-color','rgba(0,0,0,0.3)');
						break;
					case 1:
						$(v).css('background-image','url(resource/img/ico/process1.gif)');
						break;
				}
			});
		});
		var obj = this;
		this.win = $('#'+this.id).window({
			title: obj.title,
			width: obj.width,
			height: obj.height,
			top: window.innerHeight/2-obj.height/2,
			left: window.innerWidth/2-obj.width/2,
			shadow: true,
			modal: true,
			iconCls:obj.titleIco,
			closed: true,
			minimizable: false,
			maximizable: false,
			collapsible: false,
			closable:obj.closeBtn?true:false,
					resizable:obj.resize?true:false,
							//href:this.url,
							loadingMessage:true,
							inline:true,
							onBeforeClose:function(){
								if(obj.beforeClose){
									if(typeof obj.beforeClose === 'string'){
										try{
											var r =  $("#"+obj.id+" iframe")[0].contentWindow[obj.beforeClose]();
											return r;
										}catch(e){}
									}else if(typeof obj.beforeClose === 'function'){
										var str = obj.beforeClose();
										if(typeof str === 'boolean'){
											if(str === false){
												return false;
											}
										}else if(typeof str === 'string'){
											try{
												var r =  $("#"+obj.id+" iframe")[0].contentWindow[obj.beforeClose]();
												return r;
											}catch(e){}
										}
									}
								}else{
									try{
										return $("#"+obj.id+" iframe")[0].contentWindow['beforeClose']();
									}catch(e){}
								}
								return true;
							},
							onClose:function(){
								obj.destory();
							}
		});
	}
	this.win.window('open');
	//将当前对象保存到父级窗口作用域中。
	if(window.top['_data_all_'] == null){
		window.top['_data_all_'] = {};
	}
	window.top['_data_all_'][this.id]=this;
}

function destoryWindow(){
	try{
		this.win.destroy(false);
	}catch(e){}
	delete window.top['_data_all_'][this.id];
	this.destoried = true;
	$("#"+this.id).parent().next().next().remove();
	$("#"+this.id).parent().next().remove();
	$("#"+this.id).parent().remove();
	_removeG();
}

function setWidth(width){
	$("#"+id).css('width',width);
}
function setHeight(height){
	$("#"+id).css('height',height);
}

function setTitle(title){
	//$("#"+id).attr('title',title);
	this.win.setTitle(title);
}

//消息框分为 msg 消息  error 错误   alert 警告  confirm 询问
//包含的额外设置有 timeout 显示持续时长，该参数仅对msg有效,单位为秒
var DialogWindow = function(data){
	data=data||{};
	this.title = data.title||'消息';
	this.message = data.message;
	this.type = data.type||'info';
	this.timeout = data.timeout||2;
	this.accept = data.accept;
	this.unaccept = data.unaccept;
	this.timeout = this.timeout<0?0:this.timeout;
	
	var type = this.type;
	var title = this.title;
	var message = this.message;
	var timeout = this.timeout;
	var accept = this.accept;
	var unaccept = this.unaccept;
	this.show = function(){
		if(type === 'msg'){
			$.messager.show({
				title:title,
				msg:message,
				timeout : timeout*1000,
				onClose:function(){
					alert("?");
				}
			});
		}else if(type === 'confirm'){
			//_addG();
			$.messager.confirm({title:title,msg:message,fn:function(b){
				if(b){
					var c = accept?accept():'';
				}else{
					c = unaccept?unaccept():'';
				}
			}/*,onBeforeClose:_removeG*/});
		}else if(type === 'success'){
			//_addG();
			var id = new Date().getTime();
			$("body").append(
				"<div id='"+id+"' style='position:relative;z-index:999999999;padding:5px;'>" +
					"<span style='background-image:url(resource/img/ico/success-easyui.png);background-repeat:no-repeat;background-position-y:center;padding-left:38px;display:inline-block;height:32px;'>"+
						message+
					"</span>"+
				"</div>"
			);
			$('#'+id).dialog({
				title:data.title==null?'消息':data.title,
				modal: true,
				width: 300,
				height:156,
				closed:false,
				cache: false,
				onBeforeClose:function(){
					if(accept && typeof accept === 'function'){
						var b = accept();
						if(b === false){
							return false;
						}
					}
					_removeG();
					return true;
				},
				onClose:function(){
					$('#'+id).parent().next().next().remove();
					$('#'+id).parent().next().remove();
					$('#'+id).parent().remove();
				},
				buttons:[{
					text:'确定',
					width:60,
					handler:function(){
						$('#'+id).dialog('close');
					}
				}]
			});
			$('#'+id).next().css('text-align','center');
			if(timeout>0){
				setTimeout(function(){
					$('#'+id).dialog('close');
				}, timeout*1000);
			}
		}else{
			//_addG();
			$.messager.alert({
				title:title,
				msg:message,
				icon:type==='error'?'error':(
					type==='alert'?'warning':'info'
				),
				fn:accept/*,
				onBeforeClose:_removeG*/
			});
		}
	}
}
=======
//easyui dialog without html
//window所需的基本参数
//{width:窗口宽度，可以设为百分比,height:窗口高度，可以设为百分比,title:窗口标题,titleIco:窗口左上角的图标，依据easyui提供的api设置，比如需要一个空白图标，传入icon-blank。,url:要打开的页面}
//该方法用于打开一个内嵌页面，如果只是一个普通的窗口需要自定义内容，则无需如此。
//width，height，titleIcon可以省略。
var ContentWindow = function(data){
	data=data||{};
	this.width = data.width||window.innerWidth*0.9;
	//IE不支持startsWith和endsWith
	if(this.width.lastIndexOf&&this.width.lastIndexOf('%') == this.width.length-1){
		this.width = parseFloat(this.width)/100*window.innerWidth;
	}else{
		this.width = parseFloat(this.width);
	}
	this.height = data.height||window.innerHeight*0.9;
	if(this.height.lastIndexOf&&this.height.lastIndexOf('%') == this.height.length-1){
		this.height = parseInt(this.height)/100*window.innerHeight;
	}else{
		this.height = parseFloat(this.height);
	}
	this.title = data.title||'My Window';
//	this.titleIco = "iconCls:'"+(data.titleIco||"icon-blank")+"'";
	this.titleIco = data.titleIco||"icon-blank";
	this.id = new Date().getTime();
	if(data.url){
		if(data.url.indexOf('?')!=-1){
			this.url = data.url += '&' + this.id;
		}else{
			this.url = data.url += '?' + this.id;
		}
	}else{
		this.url = data.url;
	}
	this.closeBtn=typeof data.closeBtn==='boolean'?data.closeBtn:true;
	this.resize=data.resize;
	this.open=_openWindow;
	this.destory=destoryWindow;
	this.setTitle=setTitle;
	this.setWidth=setWidth;
	this.setHeight=setHeight;
	this.destoried = false;
	this.hasInit = false;
	this.buttons=[];
	this.beforeClose = data.beforeClose;
	if(data.okBtn){
		this.buttons.push({
            text: data.okBtn.text||'保存',
            iconCls: data.okBtn.iconCls||'icon-ok',
            handler: data.okBtn.handler||function(){
            	//默认调用子页面的save方法
            	if($("#"+this.id)[0].contentWindow.save)
            		$("#"+this.id)[0].contentWindow.save();
            }
        });
	}
	if(data.cancelBtn){
		this.buttons.push({
            text: data.cancelBtn.text||'关闭',
            iconCls: data.cancelBtn.iconCls||'icon-cancel',
            handler:data.cancelBtn.handler||function () {
            	obj.win.window('close');
            }
        });
	}
	this.data=data.data;
}

function initWindow(){
	/*$("body").append(
		"<div id='"+this.id+"' title='"+this.title+"' data-options=\""+this.titleIco+"\" style='width:"+this.width+";height:"+this.height+";display:none;'>" +
			"<iframe src='"+this.url+"' frameborder='no' width='100%' height='100%' marginwidth='0' marginheight='0' border='0' scrolling='no' allowtransparency='yes'>" +
			"</iframe>"+
		"</div>"
	);*/
	$("body").append(
		"<div id='"+this.id+"' style='position:relative;z-index:999999999;'>" +
			"<iframe frameborder='no' width='100%' height='100%' marginwidth='0' marginheight='0' border='0' allowtransparency='yes' style=' position:absolute;top:0;bottom:0;'>" +
			"</iframe>"+
			"<div data-role='q' style='background-color:rgba(255,255,255,0.5);position:absolute;top:0;bottom:0;left:0;right:0;'></div>"+
			"<div data-role='q' style='position:fixed;width:150px;height:20px;background-image:url(resource/img/ico/process2.gif);background-position:center;background-repeat:no-repeat;top:0;bottom:0;left:0;right:0;margin:auto;'></div>"+
		"</div>"
	);
}

//添加模糊效果：
function _addG(){
	return;
	var docum = $("body",window.top.document);
	for(var i=0;i<docum.children().length;i++){
		var d = $(docum.children()[i]);
		if(d.attr('id') == '_my97DP'){
			continue;
		}
		var num = d.attr('data-g-blur');
		d.attr('data-g-blur',isNaN(num)?1:Number(num)+1);
		d.addClass('g-blur');
	}

}

//移除模糊效果
function _removeG(){
	return;
	var docum = $("body",window.top.document);
	for(var i=0;i<docum.children().length;i++){
		var d = $(docum.children()[i]);
		if(d.attr('id') == '_my97DP'){
			continue;
		}
		var num = d.attr('data-g-blur');
		if(!isNaN(num)){
			if(num <= 1){
				d.removeClass('g-blur');
				d.removeAttr('data-g-blur');
			}else{
				d.attr('data-g-blur',--num);
			}
		}else{
			d.removeClass('g-blur');
			d.removeAttr('data-g-blur');
		}
	}
}

function _openWindow(){
	if(this.destoried){
		return;
	}
	if(!this.hasInit){
		_addG();
		initWindow.call(this);

		var arr = $("#"+this.id).find('div[data-role=q]');
		$(arr[1]).css('background-image','url(resource/img/ico/process2.gif)');
		$("#"+this.id).find('iframe').attr('src',this.url);
		$("#"+this.id).find('iframe').load(function(){
			arr.each(function(i,v){
				$(v).hide();
				switch(i){
					case 0:
						$(v).css('background-color','rgba(0,0,0,0.3)');
						break;
					case 1:
						$(v).css('background-image','url(resource/img/ico/process1.gif)');
						break;
				}
			});
		});
		var obj = this;
		this.win = $('#'+this.id).window({
			title: obj.title,
			width: obj.width,
			height: obj.height,
			top: window.innerHeight/2-obj.height/2,
			left: window.innerWidth/2-obj.width/2,
			shadow: true,
			modal: true,
			iconCls:obj.titleIco,
			closed: true,
			minimizable: false,
			maximizable: false,
			collapsible: false,
			closable:obj.closeBtn?true:false,
					resizable:obj.resize?true:false,
							//href:this.url,
							loadingMessage:true,
							inline:true,
							onBeforeClose:function(){
								if(obj.beforeClose){
									if(typeof obj.beforeClose === 'string'){
										try{
											var r =  $("#"+obj.id+" iframe")[0].contentWindow[obj.beforeClose]();
											return r;
										}catch(e){}
									}else if(typeof obj.beforeClose === 'function'){
										var str = obj.beforeClose();
										if(typeof str === 'boolean'){
											if(str === false){
												return false;
											}
										}else if(typeof str === 'string'){
											try{
												var r =  $("#"+obj.id+" iframe")[0].contentWindow[obj.beforeClose]();
												return r;
											}catch(e){}
										}
									}
								}else{
									try{
										return $("#"+obj.id+" iframe")[0].contentWindow['beforeClose']();
									}catch(e){}
								}
								return true;
							},
							onClose:function(){
								obj.destory();
							}
		});
	}
	this.win.window('open');
	//将当前对象保存到父级窗口作用域中。
	if(window.top['_data_all_'] == null){
		window.top['_data_all_'] = {};
	}
	window.top['_data_all_'][this.id]=this;
}

function destoryWindow(){
	try{
		this.win.destroy(false);
	}catch(e){}
	delete window.top['_data_all_'][this.id];
	this.destoried = true;
	$("#"+this.id).parent().next().next().remove();
	$("#"+this.id).parent().next().remove();
	$("#"+this.id).parent().remove();
	_removeG();
}

function setWidth(width){
	$("#"+id).css('width',width);
}
function setHeight(height){
	$("#"+id).css('height',height);
}

function setTitle(title){
	//$("#"+id).attr('title',title);
	this.win.setTitle(title);
}

//消息框分为 msg 消息  error 错误   alert 警告  confirm 询问
//包含的额外设置有 timeout 显示持续时长，该参数仅对msg有效,单位为秒
var DialogWindow = function(data){
	data=data||{};
	this.title = data.title||'消息';
	this.message = data.message;
	this.type = data.type||'info';
	this.timeout = data.timeout||2;
	this.accept = data.accept;
	this.unaccept = data.unaccept;
	this.timeout = this.timeout<0?0:this.timeout;
	
	var type = this.type;
	var title = this.title;
	var message = this.message;
	var timeout = this.timeout;
	var accept = this.accept;
	var unaccept = this.unaccept;
	this.show = function(){
		if(type === 'msg'){
			$.messager.show({
				title:title,
				msg:message,
				timeout : timeout*1000,
				onClose:function(){
					alert("?");
				}
			});
		}else if(type === 'confirm'){
			//_addG();
			$.messager.confirm({title:title,msg:message,fn:function(b){
				if(b){
					var c = accept?accept():'';
				}else{
					c = unaccept?unaccept():'';
				}
			}/*,onBeforeClose:_removeG*/});
		}else if(type === 'success'){
			//_addG();
			var id = new Date().getTime();
			$("body").append(
				"<div id='"+id+"' style='position:relative;z-index:999999999;padding:5px;'>" +
					"<span style='background-image:url(resource/img/ico/success-easyui.png);background-repeat:no-repeat;background-position-y:center;padding-left:38px;display:inline-block;height:32px;'>"+
						message+
					"</span>"+
				"</div>"
			);
			$('#'+id).dialog({
				title:data.title==null?'消息':data.title,
				modal: true,
				width: 300,
				height:156,
				closed:false,
				cache: false,
				onBeforeClose:function(){
					if(accept && typeof accept === 'function'){
						var b = accept();
						if(b === false){
							return false;
						}
					}
					_removeG();
					return true;
				},
				onClose:function(){
					$('#'+id).parent().next().next().remove();
					$('#'+id).parent().next().remove();
					$('#'+id).parent().remove();
				},
				buttons:[{
					text:'确定',
					width:60,
					handler:function(){
						$('#'+id).dialog('close');
					}
				}]
			});
			$('#'+id).next().css('text-align','center');
			if(timeout>0){
				setTimeout(function(){
					$('#'+id).dialog('close');
				}, timeout*1000);
			}
		}else{
			//_addG();
			$.messager.alert({
				title:title,
				msg:message,
				icon:type==='error'?'error':(
					type==='alert'?'warning':'info'
				),
				fn:accept/*,
				onBeforeClose:_removeG*/
			});
		}
	}
}
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
