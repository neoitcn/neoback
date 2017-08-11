<<<<<<< HEAD
$.extend($.fn, {
	// http://docs.jquery.com/Plugins/Validation/validate
	
	validate:function(options){
		if(!options){
			return null;
		}
		var fo = $(this);
		var rules = options.rules || {};
		
		var messages = options.messages || {};
		
		var obcss = {};
		
		var messageArr = {};
		
		var paneArr = [];
		
		this.requiredLabel=function(){
			for(var key in rules){
				var info = rules[key];
				if(info && info.required === true){
					var selector = $("[name='"+key+"']",fo.html());
					if(selector.length>0){
						$(selector).parent().append("<em style='color:#FF0000;'>*</em>");
					}
				}
			}
		};
		
		this.form = function(){
			
			for(var k in messageArr){
				var m = messageArr[k];
				$(m.control).unbind("mouseenter",m.control._a);
				$(m.control).unbind("mouseout",m.control._b);
				$(m.control).unbind("blur",m.control._c);
				$("#--"+k+"--").remove();
			}
			
			paneArr = [];
			
			messageArr={};
			
			var success = true;
			
			for(var key in rules){
				
				var accept = true;
				
				var name = key;
				
				var control = $("input[name='"+name+"']",fo.html());
				
				var val;
				
				if(control == null || control.size() == 0){
					
					control = $("textarea[name='"+name+"']",fo.html());
					
					if(control == null || control.size() == 0){
						control = $("select[name='"+name+"']",fo.html());
						if(control != null && control.size() > 0){
							val = control.val();
						}
					}else{
						val = control.val();
					}
				}else{
					val = control.val();
				}
				if(control != null && control.size() > 0){
					var valdater = rules[name];
					if(!!valdater){
						var defaultMessage = "";
						var req = valdater['required'];
						if(typeof req === 'function'){
							req = req() === true;
						}else if(typeof req !== 'boolean'){
							req = false;
						}
						var reg = valdater['reg'];
						
						var afterReg = valdater['afterReg'];
						
						var obj = this.doValidate(val,reg,afterReg,!req,control);
						
						accept = obj.accept;
						
						defaultMessage = obj.defaultMessage;
						
						control.css('border-color',obcss[control.attr('name')]);
						
						if(!accept){
							if(!obcss[name])
								obcss[name] =control.css('border-color');
							control.css('border-color','#FF0000');
							messageArr[name]={};
							if(messages[name]){
								messageArr[name].message = messages[name];
							}else{
								messageArr[name].message = defaultMessage;
							}
							messageArr[name].layoutX = control.offset().left + control.width()/2;
							messageArr[name].layoutY = control.offset().top;
							messageArr[name].control = control;
						}
					}
				}
				if(!accept){
					if(success){
						success = false;
					}
				}
			}
			return success;
		};
		
		this.showMessage = function(){
			for(var k in messageArr){
				var m = messageArr[k];
				var d;
				var topValue;
				if(messageArr[k].layoutY<30){
					d=this.style2.replace('**id**',"--"+k+"--").replace('**content**',m.message);
					topValue = $(m.control).height()+$(m.control).offset().top+6;
				}else{
					d=this.style1.replace('**id**',"--"+k+"--").replace('**content**',m.message);
					topValue = (messageArr[k].layoutY-40);
				}
				$('body').append(d);
				$("#--"+k+"--").offset({top: topValue,left:messageArr[k].layoutX - $("#--"+k+"--").width()/2});
				$("#--"+k+"--").hide();
				var innerk = k;
				m.control._a = this.privateGetShow(k,true);
				m.control._b = this.privateGetShow(k);
				
				//===============自我验证==================
				var valdater = rules[k];
				var req = false;
				var reg = null;
				if(!!valdater){
					req = valdater['required'];
					afterReg = valdater['afterReg'];
					if(typeof req === 'function'){
						req = req() === true;
					}else if(typeof req !== 'boolean'){
						req = false;
					}
					reg = valdater['reg'];
				}
				//===============自我验证==================
				m.control._c = this.privateGetChange(k,obcss[k],m.control,reg,afterReg,req,messages[k]);
				$(m.control).bind("mouseenter",m.control._a);
				$(m.control).bind("mouseout",m.control._b);
				$(m.control).bind("blur",m.control._c);
				paneArr.push("#--"+k+"--");
			}
		};
		
		return this;
	},
	style1:'<div style="position: absolute;display:inline-block;min-width: 60px;top:0px;overflow: visible;z-index:100;cursor:pointer;" id="**id**" >'+
			'<div style="position:relative;">'+
			'<div ab style="border-radius: 5px;min-height:25px;height:auto;border:1px solid #0094d9;background-color:#FFFFFF;padding:3px 5px;z-index:101;">**content**</div>'+
				'<div style="width:10px;height:10px;background-color:#FFFFFF;border-color:#0094d9;border-style:solid;border-width:0px 1px 1px 0px;  transform:rotate(45deg);margin-top:-6px;margin-left:auto;margin-right:auto;z-index:101;"></div>'+
			'</div>'+
		'</div>',
	style2:'<div style="position: absolute;display:inline-block;min-width: 60px;top:0px;overflow: visible;z-index:102;cursor:pointer;" id="**id**" >'+
			'<div style="position:relative;">'+
				'<div style="width:10px;height:10px;background-color:#FFFFFF;border-color:#0094d9;border-style:solid;border-width:1px 0px 0px 1px;transform:rotate(45deg);margin-left:auto;margin-right:auto;z-index:103;"></div>'+
				'<div ab style="border-radius: 5px;min-height:25px;height:auto;border:1px solid #0094d9;background-color:#FFFFFF;padding:3px 5px;margin-top:-6px;z-index:103;">**content**</div>'+
			'</div>'+
		'</div>',
	doValidate:function(val,reg,afterReg,allowNull,control){
		var accept = true;
		var defaultMessage = "";
		if(val==null || val==''){
			defaultMessage="该值是必须的";
			accept=allowNull;
		}else{
			if(classof(reg) === 'Regexp'){
				accept = reg.test(val);
				defaultMessage="内容不合法";
			}else if(/^gt\d+$/g.test(reg)){ //语法示范:gt5
				var length = val.length;
				var stand = reg.substring(2);
				accept = length>stand;
				defaultMessage="长度不能小于"+stand;
			}else if(/^lt\d+$/g.test(reg)){ //语法示范:lt5
				var length = val.length;
				var stand = reg.substring(2);
				accept = length<stand;
				defaultMessage="长度不能超过"+length;
			}else if(/^\d+-\d+$/g.test(reg)){ //语法示范:5-10
				var min =  Number(reg.substring(0,reg.indexOf('-')));
				var max =  Number(reg.substring(reg.indexOf('-')+1,reg.length));
				if(min>max){
					var t = min;
					min = max;
					max = t;
				}
				accept = val.length>=min && val.length <= max;
				defaultMessage="长度必须在"+min+"到"+max+"之间";
			}else if(/^zh-cn\d*-?\d*$/g.test(reg)){ //语法示范 zh-cn5-   zh-cn:-5  zh-cn:5-10
				accept = /^[\u4E00-\u9FA5\uF900-\uFA2D]+$/g.test(val);
				defaultMessage="输入中文字符不符合要求";
				if(accept){
					if(reg.length>5){
						var temp = reg.substring(5);
						if(temp.indexOf('-')!=-1){
							if(temp.length == 1){
								//zh-cn-的形式，不做任何判断。
							}else{
								var min = temp.substring(0,temp.indexOf('-'));
								var max =  temp.substring(temp.indexOf('-')+1);
								if(min && max){
									if(Number(min)> Number(max)){
										var t = min;
										min = max;
										max = t;
									}
									accept = val.length>=min && val.length<= max;
									defaultMessage="请输入"+min+"到"+max+"个汉字";
								}else if(min){
									accept = val.length >= min;
									defaultMessage="输入的个汉字不能小于"+min+"个";
								}else if(max){
									accept= val.length<= max;
									defaultMessage="输入的个汉字不能大于"+max+"个";
								}
							}
						}else{
							accept = val.length == temp;
							defaultMessage="输入的个汉字必须是"+temp+"个";
						}
					}
				}
			}else if(reg === 'email'){
				accept = /^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\.(com|cn|org|cn){1}$/g.test(val);
				defaultMessage = "电子邮件信息不合法";
			}else if(/^password\d+-\d+$/g.test(reg)){
				var min =  Number(reg.substring(8,reg.indexOf('-')));
				var max =  Number(reg.substring(reg.indexOf('-')+1,reg.length));
				if(min>max){
					var t = max;
					max = min;
					min = t;
				}
				accept = new RegExp('^[a-zA-Z0-9.,，。_-]{'+min+','+max+'}$').test(val);
				defaultMessage="密码长度只能是"+min+"到"+max+"之间，只能包含数字，字母和.,，。_-";
			}else if(reg === 'phone'){
				accept = /^\d{11}$/g.test(val);
				defaultMessage="手机号码不合法";
			}else if('integer' === reg){
				accept = /^-?\d+$/g.test(val);
				defaultMessage="必须是一个整数";
			}else if(/^unsign((-\d+)|(\d+(-\d+)?)?|\d+-)?$/g.test(reg)){
				accept = /^\d+$/g.test(val);
				defaultMessage="必须是一个不为负数的整数";
				if(accept){
					var regStr = reg.substring(6);
					var regArr = regStr.split('-');
					if(regArr[0] && regArr[1]){
						var max = Number(regArr[1]);
						var min = Number(regArr[0]);
						if(min>max){
							var t = min;
							min = max;
							max = t;
						}
						accept = val>=min && val <= max;
						defaultMessage="数值必须大于等于"+min+"且小于等于"+max;
					}else if(regArr[0]){
						var min = Number(regArr[0]);
						accept = val>=min;
						defaultMessage="数值必须大于等于"+min;
					}else if(regArr[1]){
						var max = Number(regArr[1]);
						accept = val<=max;
						defaultMessage = "数值必须小于等于"+max;
					}
				}
			}else if(reg === 'double'){
				accept = /^-?\d+(\.{1}\d+)?$/g.test(val);
				defaultMessage="数字不合法";
			}else if(reg === 'unsignDouble'){
				accept = /^\d+(\.{1}\d+)?$/g.test(val);
				defaultMessage="必须是一个不小于0的数字";
			}else if(reg === 'idCard'){
				accept = idCardNoUtil.checkIdCardNo(val);
				defaultMessage = "身份证信息不合法";
			}else if(typeof reg === 'function'){
				var result = reg(val,control);
				if( result === false){
					accept = false;
				}else{
					if(typeof result === 'string'){
						return this.doValidate(val,result,afterReg,allowNull,control);
					}
					if(result === undefined){
						accept = true;
					}
				}
			}
			if(accept){
				if(afterReg){
					var afObj = afterReg(val);
					if(typeof afObj === 'boolean'){
						accept = afObj;
						defaultMessage="验证失败";
					}else{
						accept = afObj['accept'];
						defaultMessage = afObj['message'];
					}
				}
			}
		}
		return {accept:accept,defaultMessage:defaultMessage};
	},
	privateGetShow:function(k,b){
		return function(e){
			if(b)
				$("#--"+k+"--").show();
			else
				$("#--"+k+"--").hide();
		};
	},
	privateGetChange:function(k,color,m,reg,afterReg,req,message){
		var thisObj = this;
		return function(e){
			var obj = thisObj.doValidate($(m).val(),reg,afterReg,!req,m);
			if(obj.accept === true){
				$(this).css('border-color',color);
				$(m).unbind("mouseenter",m._a);
				$(m).unbind("mouseout",m._b);
				$("#--"+k+"--").remove();
			}else{
				if(message){
					$("#--"+k+"-- div[ab]").text(message);
				}else{
					$("#--"+k+"-- div[ab]").text(obj.defaultMessage);
				}
			}
		};
	}
		
})

	var idCardNoUtil = {
			provinceAndCitys: {
				11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",
				31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",
				45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",
				65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"
			},

			powers: ["7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"],

			parityBit: ["1","0","X","9","8","7","6","5","4","3","2"],

			genders: {male:"男",female:"女"},

			checkAddressCode: function(addressCode){
				var check = /^[1-9]\d{5}$/.test(addressCode);
				if(!check) return false;
				if(idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0,2))]){
					return true;
				}
				else{
					return false;
				}
			},

			checkBirthDayCode: function(birDayCode){
				var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(birDayCode);
				if(!check) return false;
				var yyyy = parseInt(birDayCode.substring(0,4),10);
				var mm = parseInt(birDayCode.substring(4,6),10);
				var dd = parseInt(birDayCode.substring(6),10);
				var xdata = new Date(yyyy,mm-1,dd);
				if(xdata > new Date()){
					return false;//生日不能大于当前日期
				}
				else if ( ( xdata.getFullYear() == yyyy ) && ( xdata.getMonth () == mm - 1 ) && ( xdata.getDate() == dd ) ){
					return true;
				}
				else{
					return false;
				}
			},

			getParityBit: function(idCardNo){
				var id17 = idCardNo.substring(0,17);	
				var power = 0;
				for(var i=0;i<17;i++){
					power += parseInt(id17.charAt(i),10) * parseInt(idCardNoUtil.powers[i]);
				}
				
				var mod = power % 11;
				return idCardNoUtil.parityBit[mod];
			},

			checkParityBit: function(idCardNo){
				var parityBit = idCardNo.charAt(17).toUpperCase();
				if(idCardNoUtil.getParityBit(idCardNo) == parityBit){
					return true;
				}
				else{
					return false;
				}
			},

			checkIdCardNo: function(idCardNo){
				//15位和18位身份证号码的基本校验
				var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
				if(!check) return false;
				//判断长度为15位或18位
				if(idCardNo.length==15){
					return idCardNoUtil.check15IdCardNo(idCardNo);
				}
				else if(idCardNo.length==18){
					return idCardNoUtil.check18IdCardNo(idCardNo);
				}
				else{
					return false;
				}
			},

			//校验15位的身份证号码
			check15IdCardNo: function(idCardNo){
				//15位身份证号码的基本校验
				var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(idCardNo);
				if(!check) return false;
				//校验地址码
				var addressCode = idCardNo.substring(0,6);
				check = idCardNoUtil.checkAddressCode(addressCode);
				if(!check) return false;
				var birDayCode = '19' + idCardNo.substring(6,12);
				//校验日期码
				return idCardNoUtil.checkBirthDayCode(birDayCode);
			},

			//校验18位的身份证号码
			check18IdCardNo: function(idCardNo){
				//18位身份证号码的基本格式校验
				var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(idCardNo);
				if(!check) return false;
				//校验地址码
				var addressCode = idCardNo.substring(0,6);
				check = idCardNoUtil.checkAddressCode(addressCode);
				if(!check) return false;
				//校验日期码
				var birDayCode = idCardNo.substring(6,14);
				check = idCardNoUtil.checkBirthDayCode(birDayCode);
				if(!check) return false;
				//验证校检码
				return idCardNoUtil.checkParityBit(idCardNo);
			},

			formateDateCN: function(day){
				var yyyy =day.substring(0,4);
				var mm = day.substring(4,6);
				var dd = day.substring(6);
				return yyyy + '-' + mm +'-' + dd;
			},

			//获取信息
			getIdCardInfo: function(idCardNo){
				var idCardInfo = {
					gender:"", //性别
					birthday:"" // 出生日期(yyyy-mm-dd)
				};
				if(idCardNo.length==15){
					var aday = '19' + idCardNo.substring(6,12);
					idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
					if(parseInt(idCardNo.charAt(14))%2==0){
					idCardInfo.gender=idCardNoUtil.genders.female;
					}else{
					idCardInfo.gender=idCardNoUtil.genders.male;
					}
				}
				else if(idCardNo.length==18){
					var aday = idCardNo.substring(6,14);
					idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
					if(parseInt(idCardNo.charAt(16))%2==0){
						idCardInfo.gender=idCardNoUtil.genders.female;
					}else{
						idCardInfo.gender=idCardNoUtil.genders.male;
					}
				}
				return idCardInfo;
			},

			getId15:function(idCardNo){
				if(idCardNo.length==15){
					return idCardNo;
				}
				else if(idCardNo.length==18){
					return idCardNo.substring(0,6) + idCardNo.substring(8,17);
				}
				else{
					return null;
				}
			},

			getId18: function(idCardNo){
				if(idCardNo.length==15){
					var id17 = idCardNo.substring(0,6) + '19' + idCardNo.substring(6);
					var parityBit = idCardNoUtil.getParityBit(id17);
					return id17 + parityBit;
				}
				else if(idCardNo.length==18){
					return idCardNo;
				}
				else{
					return null;
				}
			}
			
		}
	
	function classof(o){
		if(o === null){
			return 'NULL';
		}
		if(o === undefined){
			return 'Undefined';
		}
		return Object.prototype.toString.call(o).slice(8,-1);
=======
$.extend($.fn, {
	// http://docs.jquery.com/Plugins/Validation/validate
	
	validate:function(options){
		if(!options){
			return null;
		}
		var fo = $(this);
		var rules = options.rules || {};
		
		var messages = options.messages || {};
		
		var obcss = {};
		
		var messageArr = {};
		
		var paneArr = [];
		
		this.requiredLabel=function(){
			for(var key in rules){
				var info = rules[key];
				if(info && info.required === true){
					var selector = $("[name='"+key+"']",fo.html());
					if(selector.length>0){
						$(selector).parent().append("<em style='color:#FF0000;'>*</em>");
					}
				}
			}
		};
		
		this.form = function(){
			
			for(var k in messageArr){
				var m = messageArr[k];
				$(m.control).unbind("mouseenter",m.control._a);
				$(m.control).unbind("mouseout",m.control._b);
				$(m.control).unbind("blur",m.control._c);
				$("#--"+k+"--").remove();
			}
			
			paneArr = [];
			
			messageArr={};
			
			var success = true;
			
			for(var key in rules){
				
				var accept = true;
				
				var name = key;
				
				var control = $("input[name='"+name+"']",fo.html());
				
				var val;
				
				if(control == null || control.size() == 0){
					
					control = $("textarea[name='"+name+"']",fo.html());
					
					if(control == null || control.size() == 0){
						control = $("select[name='"+name+"']",fo.html());
						if(control != null && control.size() > 0){
							val = control.val();
						}
					}else{
						val = control.val();
					}
				}else{
					val = control.val();
				}
				if(control != null && control.size() > 0){
					var valdater = rules[name];
					if(!!valdater){
						var defaultMessage = "";
						var req = valdater['required'];
						if(typeof req === 'function'){
							req = req() === true;
						}else if(typeof req !== 'boolean'){
							req = false;
						}
						var reg = valdater['reg'];
						
						var afterReg = valdater['afterReg'];
						
						var obj = this.doValidate(val,reg,afterReg,!req,control);
						
						accept = obj.accept;
						
						defaultMessage = obj.defaultMessage;
						
						control.css('border-color',obcss[control.attr('name')]);
						
						if(!accept){
							if(!obcss[name])
								obcss[name] =control.css('border-color');
							control.css('border-color','#FF0000');
							messageArr[name]={};
							if(messages[name]){
								messageArr[name].message = messages[name];
							}else{
								messageArr[name].message = defaultMessage;
							}
							messageArr[name].layoutX = control.offset().left + control.width()/2;
							messageArr[name].layoutY = control.offset().top;
							messageArr[name].control = control;
						}
					}
				}
				if(!accept){
					if(success){
						success = false;
					}
				}
			}
			return success;
		};
		
		this.showMessage = function(){
			for(var k in messageArr){
				var m = messageArr[k];
				var d;
				var topValue;
				if(messageArr[k].layoutY<30){
					d=this.style2.replace('**id**',"--"+k+"--").replace('**content**',m.message);
					topValue = $(m.control).height()+$(m.control).offset().top+6;
				}else{
					d=this.style1.replace('**id**',"--"+k+"--").replace('**content**',m.message);
					topValue = (messageArr[k].layoutY-40);
				}
				$('body').append(d);
				$("#--"+k+"--").offset({top: topValue,left:messageArr[k].layoutX - $("#--"+k+"--").width()/2});
				$("#--"+k+"--").hide();
				var innerk = k;
				m.control._a = this.privateGetShow(k,true);
				m.control._b = this.privateGetShow(k);
				
				//===============自我验证==================
				var valdater = rules[k];
				var req = false;
				var reg = null;
				if(!!valdater){
					req = valdater['required'];
					afterReg = valdater['afterReg'];
					if(typeof req === 'function'){
						req = req() === true;
					}else if(typeof req !== 'boolean'){
						req = false;
					}
					reg = valdater['reg'];
				}
				//===============自我验证==================
				m.control._c = this.privateGetChange(k,obcss[k],m.control,reg,afterReg,req,messages[k]);
				$(m.control).bind("mouseenter",m.control._a);
				$(m.control).bind("mouseout",m.control._b);
				$(m.control).bind("blur",m.control._c);
				paneArr.push("#--"+k+"--");
			}
		};
		
		return this;
	},
	style1:'<div style="position: absolute;display:inline-block;min-width: 60px;top:0px;overflow: visible;z-index:100;cursor:pointer;" id="**id**" >'+
			'<div style="position:relative;">'+
			'<div ab style="border-radius: 5px;min-height:25px;height:auto;border:1px solid #0094d9;background-color:#FFFFFF;padding:3px 5px;z-index:101;">**content**</div>'+
				'<div style="width:10px;height:10px;background-color:#FFFFFF;border-color:#0094d9;border-style:solid;border-width:0px 1px 1px 0px;  transform:rotate(45deg);margin-top:-6px;margin-left:auto;margin-right:auto;z-index:101;"></div>'+
			'</div>'+
		'</div>',
	style2:'<div style="position: absolute;display:inline-block;min-width: 60px;top:0px;overflow: visible;z-index:102;cursor:pointer;" id="**id**" >'+
			'<div style="position:relative;">'+
				'<div style="width:10px;height:10px;background-color:#FFFFFF;border-color:#0094d9;border-style:solid;border-width:1px 0px 0px 1px;transform:rotate(45deg);margin-left:auto;margin-right:auto;z-index:103;"></div>'+
				'<div ab style="border-radius: 5px;min-height:25px;height:auto;border:1px solid #0094d9;background-color:#FFFFFF;padding:3px 5px;margin-top:-6px;z-index:103;">**content**</div>'+
			'</div>'+
		'</div>',
	doValidate:function(val,reg,afterReg,allowNull,control){
		var accept = true;
		var defaultMessage = "";
		if(val==null || val==''){
			defaultMessage="该值是必须的";
			accept=allowNull;
		}else{
			if(classof(reg) === 'Regexp'){
				accept = reg.test(val);
				defaultMessage="内容不合法";
			}else if(/^gt\d+$/g.test(reg)){ //语法示范:gt5
				var length = val.length;
				var stand = reg.substring(2);
				accept = length>stand;
				defaultMessage="长度不能小于"+stand;
			}else if(/^lt\d+$/g.test(reg)){ //语法示范:lt5
				var length = val.length;
				var stand = reg.substring(2);
				accept = length<stand;
				defaultMessage="长度不能超过"+length;
			}else if(/^\d+-\d+$/g.test(reg)){ //语法示范:5-10
				var min =  Number(reg.substring(0,reg.indexOf('-')));
				var max =  Number(reg.substring(reg.indexOf('-')+1,reg.length));
				if(min>max){
					var t = min;
					min = max;
					max = t;
				}
				accept = val.length>=min && val.length <= max;
				defaultMessage="长度必须在"+min+"到"+max+"之间";
			}else if(/^zh-cn\d*-?\d*$/g.test(reg)){ //语法示范 zh-cn5-   zh-cn:-5  zh-cn:5-10
				accept = /^[\u4E00-\u9FA5\uF900-\uFA2D]+$/g.test(val);
				defaultMessage="输入中文字符不符合要求";
				if(accept){
					if(reg.length>5){
						var temp = reg.substring(5);
						if(temp.indexOf('-')!=-1){
							if(temp.length == 1){
								//zh-cn-的形式，不做任何判断。
							}else{
								var min = temp.substring(0,temp.indexOf('-'));
								var max =  temp.substring(temp.indexOf('-')+1);
								if(min && max){
									if(Number(min)> Number(max)){
										var t = min;
										min = max;
										max = t;
									}
									accept = val.length>=min && val.length<= max;
									defaultMessage="请输入"+min+"到"+max+"个汉字";
								}else if(min){
									accept = val.length >= min;
									defaultMessage="输入的个汉字不能小于"+min+"个";
								}else if(max){
									accept= val.length<= max;
									defaultMessage="输入的个汉字不能大于"+max+"个";
								}
							}
						}else{
							accept = val.length == temp;
							defaultMessage="输入的个汉字必须是"+temp+"个";
						}
					}
				}
			}else if(reg === 'email'){
				accept = /^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\.(com|cn|org|cn){1}$/g.test(val);
				defaultMessage = "电子邮件信息不合法";
			}else if(/^password\d+-\d+$/g.test(reg)){
				var min =  Number(reg.substring(8,reg.indexOf('-')));
				var max =  Number(reg.substring(reg.indexOf('-')+1,reg.length));
				if(min>max){
					var t = max;
					max = min;
					min = t;
				}
				accept = new RegExp('^[a-zA-Z0-9.,，。_-]{'+min+','+max+'}$').test(val);
				defaultMessage="密码长度只能是"+min+"到"+max+"之间，只能包含数字，字母和.,，。_-";
			}else if(reg === 'phone'){
				accept = /^\d{11}$/g.test(val);
				defaultMessage="手机号码不合法";
			}else if('integer' === reg){
				accept = /^-?\d+$/g.test(val);
				defaultMessage="必须是一个整数";
			}else if(/^unsign((-\d+)|(\d+(-\d+)?)?|\d+-)?$/g.test(reg)){
				accept = /^\d+$/g.test(val);
				defaultMessage="必须是一个不为负数的整数";
				if(accept){
					var regStr = reg.substring(6);
					var regArr = regStr.split('-');
					if(regArr[0] && regArr[1]){
						var max = Number(regArr[1]);
						var min = Number(regArr[0]);
						if(min>max){
							var t = min;
							min = max;
							max = t;
						}
						accept = val>=min && val <= max;
						defaultMessage="数值必须大于等于"+min+"且小于等于"+max;
					}else if(regArr[0]){
						var min = Number(regArr[0]);
						accept = val>=min;
						defaultMessage="数值必须大于等于"+min;
					}else if(regArr[1]){
						var max = Number(regArr[1]);
						accept = val<=max;
						defaultMessage = "数值必须小于等于"+max;
					}
				}
			}else if(reg === 'double'){
				accept = /^-?\d+(\.{1}\d+)?$/g.test(val);
				defaultMessage="数字不合法";
			}else if(reg === 'unsignDouble'){
				accept = /^\d+(\.{1}\d+)?$/g.test(val);
				defaultMessage="必须是一个不小于0的数字";
			}else if(reg === 'idCard'){
				accept = idCardNoUtil.checkIdCardNo(val);
				defaultMessage = "身份证信息不合法";
			}else if(typeof reg === 'function'){
				var result = reg(val,control);
				if( result === false){
					accept = false;
				}else{
					if(typeof result === 'string'){
						return this.doValidate(val,result,afterReg,allowNull,control);
					}
					if(result === undefined){
						accept = true;
					}
				}
			}
			if(accept){
				if(afterReg){
					var afObj = afterReg(val);
					if(typeof afObj === 'boolean'){
						accept = afObj;
						defaultMessage="验证失败";
					}else{
						accept = afObj['accept'];
						defaultMessage = afObj['message'];
					}
				}
			}
		}
		return {accept:accept,defaultMessage:defaultMessage};
	},
	privateGetShow:function(k,b){
		return function(e){
			if(b)
				$("#--"+k+"--").show();
			else
				$("#--"+k+"--").hide();
		};
	},
	privateGetChange:function(k,color,m,reg,afterReg,req,message){
		var thisObj = this;
		return function(e){
			var obj = thisObj.doValidate($(m).val(),reg,afterReg,!req,m);
			if(obj.accept === true){
				$(this).css('border-color',color);
				$(m).unbind("mouseenter",m._a);
				$(m).unbind("mouseout",m._b);
				$("#--"+k+"--").remove();
			}else{
				if(message){
					$("#--"+k+"-- div[ab]").text(message);
				}else{
					$("#--"+k+"-- div[ab]").text(obj.defaultMessage);
				}
			}
		};
	}
		
})

	var idCardNoUtil = {
			provinceAndCitys: {
				11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",
				31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",
				45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",
				65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"
			},

			powers: ["7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"],

			parityBit: ["1","0","X","9","8","7","6","5","4","3","2"],

			genders: {male:"男",female:"女"},

			checkAddressCode: function(addressCode){
				var check = /^[1-9]\d{5}$/.test(addressCode);
				if(!check) return false;
				if(idCardNoUtil.provinceAndCitys[parseInt(addressCode.substring(0,2))]){
					return true;
				}
				else{
					return false;
				}
			},

			checkBirthDayCode: function(birDayCode){
				var check = /^[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))$/.test(birDayCode);
				if(!check) return false;
				var yyyy = parseInt(birDayCode.substring(0,4),10);
				var mm = parseInt(birDayCode.substring(4,6),10);
				var dd = parseInt(birDayCode.substring(6),10);
				var xdata = new Date(yyyy,mm-1,dd);
				if(xdata > new Date()){
					return false;//生日不能大于当前日期
				}
				else if ( ( xdata.getFullYear() == yyyy ) && ( xdata.getMonth () == mm - 1 ) && ( xdata.getDate() == dd ) ){
					return true;
				}
				else{
					return false;
				}
			},

			getParityBit: function(idCardNo){
				var id17 = idCardNo.substring(0,17);	
				var power = 0;
				for(var i=0;i<17;i++){
					power += parseInt(id17.charAt(i),10) * parseInt(idCardNoUtil.powers[i]);
				}
				
				var mod = power % 11;
				return idCardNoUtil.parityBit[mod];
			},

			checkParityBit: function(idCardNo){
				var parityBit = idCardNo.charAt(17).toUpperCase();
				if(idCardNoUtil.getParityBit(idCardNo) == parityBit){
					return true;
				}
				else{
					return false;
				}
			},

			checkIdCardNo: function(idCardNo){
				//15位和18位身份证号码的基本校验
				var check = /^\d{15}|(\d{17}(\d|x|X))$/.test(idCardNo);
				if(!check) return false;
				//判断长度为15位或18位
				if(idCardNo.length==15){
					return idCardNoUtil.check15IdCardNo(idCardNo);
				}
				else if(idCardNo.length==18){
					return idCardNoUtil.check18IdCardNo(idCardNo);
				}
				else{
					return false;
				}
			},

			//校验15位的身份证号码
			check15IdCardNo: function(idCardNo){
				//15位身份证号码的基本校验
				var check = /^[1-9]\d{7}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}$/.test(idCardNo);
				if(!check) return false;
				//校验地址码
				var addressCode = idCardNo.substring(0,6);
				check = idCardNoUtil.checkAddressCode(addressCode);
				if(!check) return false;
				var birDayCode = '19' + idCardNo.substring(6,12);
				//校验日期码
				return idCardNoUtil.checkBirthDayCode(birDayCode);
			},

			//校验18位的身份证号码
			check18IdCardNo: function(idCardNo){
				//18位身份证号码的基本格式校验
				var check = /^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))\d{3}(\d|x|X)$/.test(idCardNo);
				if(!check) return false;
				//校验地址码
				var addressCode = idCardNo.substring(0,6);
				check = idCardNoUtil.checkAddressCode(addressCode);
				if(!check) return false;
				//校验日期码
				var birDayCode = idCardNo.substring(6,14);
				check = idCardNoUtil.checkBirthDayCode(birDayCode);
				if(!check) return false;
				//验证校检码
				return idCardNoUtil.checkParityBit(idCardNo);
			},

			formateDateCN: function(day){
				var yyyy =day.substring(0,4);
				var mm = day.substring(4,6);
				var dd = day.substring(6);
				return yyyy + '-' + mm +'-' + dd;
			},

			//获取信息
			getIdCardInfo: function(idCardNo){
				var idCardInfo = {
					gender:"", //性别
					birthday:"" // 出生日期(yyyy-mm-dd)
				};
				if(idCardNo.length==15){
					var aday = '19' + idCardNo.substring(6,12);
					idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
					if(parseInt(idCardNo.charAt(14))%2==0){
					idCardInfo.gender=idCardNoUtil.genders.female;
					}else{
					idCardInfo.gender=idCardNoUtil.genders.male;
					}
				}
				else if(idCardNo.length==18){
					var aday = idCardNo.substring(6,14);
					idCardInfo.birthday=idCardNoUtil.formateDateCN(aday);
					if(parseInt(idCardNo.charAt(16))%2==0){
						idCardInfo.gender=idCardNoUtil.genders.female;
					}else{
						idCardInfo.gender=idCardNoUtil.genders.male;
					}
				}
				return idCardInfo;
			},

			getId15:function(idCardNo){
				if(idCardNo.length==15){
					return idCardNo;
				}
				else if(idCardNo.length==18){
					return idCardNo.substring(0,6) + idCardNo.substring(8,17);
				}
				else{
					return null;
				}
			},

			getId18: function(idCardNo){
				if(idCardNo.length==15){
					var id17 = idCardNo.substring(0,6) + '19' + idCardNo.substring(6);
					var parityBit = idCardNoUtil.getParityBit(id17);
					return id17 + parityBit;
				}
				else if(idCardNo.length==18){
					return idCardNo;
				}
				else{
					return null;
				}
			}
			
		}
	
	function classof(o){
		if(o === null){
			return 'NULL';
		}
		if(o === undefined){
			return 'Undefined';
		}
		return Object.prototype.toString.call(o).slice(8,-1);
>>>>>>> 17ac48076d7a7be7437c8367312af01562698b1c
	}