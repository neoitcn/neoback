$(document).ready(function() {
	window.paWin = window;
	if(!paWin.$) {
		window.paWin = window.myPaWin;
	}
	var obj = paWin.$('#messageContainer');
	if(obj.size() == 0) {
		 //创建div
	    var newDiv = paWin.$("<div/>").attr("id", "messageContainer")
	    						.css('overflow', 'auto')
	    						.css('position', 'absolute')
			                    .css("border", "1px solid #dfddd8")
			                    .css("background", "#FFFFFF")
			                    .css('display', 'none')
			                    .css('text-align', 'left')
			                    .css('z-index', '10000000')
	    						.css('padding', '5px');
	    paWin.$(paWin.document.body).append(newDiv);
	}
});
(function($){
	var mouseX,mouseY;
	$(document).mousemove(function (e) {
        mouseX = e.pageX;
        mouseY = e.pageY;
    });
	$.extend($.validator.prototype, {
		'showLabel': function(element, message) {
			element.onmouseover = function() {
				var classStr = $(element).attr('class');
				if(classStr.indexOf('error') == -1) {
					return;
				}
				var html = this.nextSibling.title;
				
				window.paWin.document.getElementById('messageContainer').innerHTML= html;
//				var width = paWin.document.body.clientWidth;
//				if(event.screenX > (paWin.screenTop + width - 50)){
//					paWin.document.getElementById('messageContainer').style.left = (width - 100) + "px";
//				}else{
//					paWin.document.getElementById('messageContainer').style.left = (event.screenX - paWin.screenLeft) + "px";
//				}
//				paWin.document.getElementById('messageContainer').style.top = (event.screenY - paWin.screenTop) + "px";
//				var obj = $(element);
//				var offset = $(element).offset();
//				$("#messageContainer").css({left:offset.left+obj.width()+10 + "px", top:offset.top + obj.outerHeight()-obj.height()-5 + "px",color:"#F00"});
				$("#messageContainer").css({color:"#F00"});
				paWin.document.getElementById('messageContainer').style.left = mouseX + "px";
				paWin.document.getElementById('messageContainer').style.top = mouseY + "px";
				
				window.paWin.document.getElementById('messageContainer').style.display = 'block';
			};
			element.onmouseout = function(event) {
				window.paWin.document.getElementById('messageContainer').style.display = "none";
			};
			var label = this.errorsFor(element);
			if(label.length) {
				// refresh error/success class
				label.removeClass().addClass( this.settings.errorClass );
			
				// check if we have a generated label, replace the message then
				label.attr("generated") && label.html(" ");
				label.attr("title",message);
				label.attr("style","float: left;");
			}else {
				//create label
				label = $("<" + this.settings.errorElement + "/>")
					.attr({"for":  this.idOrName(element), generated: true})
					.addClass(this.settings.errorClass)
					.html(" ");
				label.attr("title",message);
				label.attr("style","float: left;");
				if ( this.settings.wrapper ) {
					// make sure the element is visible, even in IE
					// actually showing the wrapped element is handled elsewhere
					//label = label.hide().show().wrap("<" + this.settings.wrapper + "/>").parent();
				}
				if ( !this.labelContainer.append(label).length )
					this.settings.errorPlacement
						? this.settings.errorPlacement(label, $(element) )
						: label.insertAfter(element);
			}
			if ( !message && this.settings.success ) {
				label.text("");
				typeof this.settings.success == "string"
					? label.addClass( this.settings.success )
					: this.settings.success( label );
			}
			this.toShow = this.toShow.add(label);
		},
		'hideErrors': function() {
		    $("#messageContainer").hide();
		    this.addWrapper( this.toHide ).hide();
		}
	});
	
	
	//////////////////////////////////////////////////////////////////////
	// 身份证号码验证
	//////////////////////////////////////////////////////////////////////
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
			
		};
	
	
	
	//////////////////////////////////////////////////////////////////////
	// 验证规则的扩展
	
	// # 表示该处至少得输入字母，数字，- 或_等字符，其它的特殊字符则不行
	$.validator.addMethod("letterNumber", function(value,element){
		return this.optional(element) || /^([a-zA-Z0-9_-]+)$/.test(value);
	},  " 只能输入字母、数字、-、_等字符。");
	
	// # 表示该处只能输入第一个字母为大写，后面是字母或数字的字符串，如'Asd123'等，其它的特殊字符或汉字则不行
	$.validator.addMethod("EoName", function(value,element){
		return this.optional(element) || /^([A-Z]{1})([a-zA-Z0-9]*)$/.test(value);
	},  " 必须以大写字母开头，只能用英文字母和数字。");

	/*
	 * # 用法："fieldName":{stringMinLength:20}
	 * # 表示该处至少得输入20个字符，{0}代表至少要输入的字符长度大小。字母、数字、特殊字符各占一个字符，一个汉字占两个字符
	 * # 如'中'的长度为2，'a'的长度为1，'中国ab12#!'的长度为10。
	 */
	$.validator.addMethod("stringMinLength", function(value, element, param) {   
		var length = value.length;   
		for ( var i = 0; i < value.length; i++) {   
			if (value.charCodeAt(i) > 127) {   
				length++;   
			}   
		}   
		return this.optional(element) || (length >= param);   
	}, $.validator.format(" 长度不能小于{0} , 一个汉字占2个字符"));

	/*
	 * # 用法："fieldName":{stringMaxLength:20}
	 * # 表示该处输入的汉字或字符的总长度不能超过20，{0}代表不能超过的长度大小。
	 * # 字母、数字、特殊字符各占一个字符，一个汉字占两个字符，如'中'的长度为2，'a'的长度为1，'中国ab12#!'的长度为10
	 */ 
	$.validator.addMethod("stringMaxLength", function(value, element, param) { 
		var length = value.length;   
		for ( var i = 0; i < value.length; i++) {  
			if (value.charCodeAt(i) > 127) {   
			length++;   
			}   
		}   
		return this.optional(element) || (length <= param);   
	}, $.validator.format(" 长度不能大于{0} , 一个汉字占2个字符"));

	/*
	 * # 用法："fieldName":{string:true}
	 * # 表示该处只能输入一般的字符串或数字,如a,b,c,1,2,3等,也可以输入中文,但不能输入包含特殊的字符,
	 * # 如'$'，'#'，'^'，'('，'!'等特殊字符,也不能输入空字符串。
	 */
	$.validator.addMethod("string", function(value, element) {   
		return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);   
	}, " 不允许包含特殊符号!");   
	 

	/*
	 * # stringCH：只能输入汉字
	 * # 用法："fieldName":{stringCH:true}
	 * # 表示该处只能输入汉字（一个汉字占两个字节），不过输入空字符串或特殊字符后也能通过校验，因此它一般与required，string一起使用。
	 */
	$.validator.addMethod("stringCH", function(value, element) {   
		var length = value.length;   
		for(var i = 0; i < value.length; i++){   
			if(value.charCodeAt(i) > 127){   
				length++;   
			}   
		}   
		return this.optional(element) || /[^u4E00-u9FA5]/g.test(value);   
	}, " 只能输入汉字,一个汉字占两具字节");   

	/*
	 * # stringEN：只能输入字母
	 * # 用法："fieldName":{stringEN:true}
	 * # 表示该处只能输入纯的字母，不过输入空字符串后也能通过校验，因此它一般与required一起使用。
	 */
	$.validator.addMethod("stringEN", function(value, element) {   
		var length = value.length;   
		for(var i = 0; i < value.length; i++){   
			if(value.charCodeAt(i) > 127){   
				length++;   
			}
		} 
		return this.optional(element) || /^[A-Za-z]+$/g.test(value);   
	}, " 只能输入字母");
	
	/*
	 * # isZipCode：请填写正确的邮政编码。
	 * # 用法："fieldName":{isZipCode:true} 
	 * # 表示该处为邮政编码的校验，只允许输入6个纯数字，其它的均不合法。不过输入空字符串后也能通过校验。
	 */
	$.validator.addMethod("isZipCode", function(value, element) {    
		var tel = /^[0-9]{6}$/;    
		return this.optional(element) || (tel.test(value));    
	}, " 请正确填写邮政编码");    
	
	// 只输入时间日期字段  
	$.validator.addMethod("datetime", function(value, element) {   
		return this.optional(element) || /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/.test(value);   
	}, " 请正确填写时间日期!例：2011-01-01 01:10:12");   
	
	// 电话号码验证
	jQuery.validator.addMethod("phone", function(value, element) { 
		var tel = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/; 
		return this.optional(element) || (tel.test(value)); 
	}, "电话号码格式错误");
	
	// 手机号码验证
	jQuery.validator.addMethod("mobile", function(value, element) { 
		var length = value.length; 
		var mobile = /^1(3[0-9]|5[7-9]|53|56|82|8[6-9])[0-9]{8}$/;
			return this.optional(element) || (length == 11 && mobile.test(value)); 
	}, "手机号码格式错误");
	
	// 手机与电话号码共同的验证
	jQuery.validator.addMethod("iphone", function(value, element) { 
		var length = value.length;
		var tel = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
		var mobile = /^1(3[0-9]|5[7-9]|53|56|82|8[6-9])[0-9]{8}$/;
			return this.optional(element) || ((length == 11 && mobile.test(value)) || (tel.test(value))); 
	}, "电话号码或手机号码格式错误");
	
	// 身份证号码验证
	jQuery.validator.addMethod("isIdCardNo", function(value, element) { 
		  return this.optional(element) || idCardNoUtil.checkIdCardNo(value);     
		}, "请正确输入的身份证号码"); 
	
	// 正实数(包括正整数与正小数)验证
	jQuery.validator.addMethod("positiveRealNum", function(value, element) { 
		return this.optional(element) || (/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test(value) && value>=0);     
	}, "请输入正数(包括正整数或正小数)"); 
	// 日期验证
	jQuery.validator.addMethod("jccpDate", function(value, element) { 
		return this.optional(element) || (/(((^((1[8-9]\d{2})|([2-9]\d{3}))-(10|12|0?[13578])-(3[01]|[12][0-9]|0?[1-9]))|(^((1[8-9]\d{2})|([2-9]\d{3}))-(11|0?[469])-(30|[12][0-9]|0?[1-9]))|(^((1[8-9]\d{2})|([2-9]\d{3}))-(0?2)-(2[0-8]|1[0-9]|0?[1-9]))|(^([2468][048]00)-(0?2)-(29))|(^([3579][26]00)-(0?2)-(29))|(^([1][89][0][48])-(0?2)-(29))|(^([2-9][0-9][0][48])-(0?2)-(29))|(^([1][89][2468][048])-(0?2)-(29))|(^([2-9][0-9][2468][048])-(0?2)-(29))|(^([1][89][13579][26])-(0?2)-(29))|(^([2-9][0-9][13579][26])-(0?2)-(29)))((\s+(0?[1-9]|1[012])(:[0-5]\d){0,2}(\s[AP]M))?$|(\s+([01]\d|2[0-3])(:[0-5]\d){0,2})?$))/.test(value));     
	}, "请输入合法的日期(格式：yyyy-mm-dd)");
	
	// 数字(包括长度不能超过10位，其中小数点位数不大于3位)验证
	jQuery.validator.addMethod("newNum", function(value, element) { 
		return this.optional(element) || (/^\d{1,10}(\.\d{1,3})?$/.test(value));     
	}, "请输入数字(包括长度不能超过10位，其中小数点位数不大于3位)"); 
	// 数字(包括长度不能超过16位，其中小数点位数不大于4位)验证
	jQuery.validator.addMethod("newNum2", function(value, element) { 
		return this.optional(element) || (/^\d{1,16}(\.\d{1,4})?$/.test(value));      
	}, "请输入数字(包括长度不能超过16位，其中小数点位数不大于4位)"); 
	// 数字(包括长度不能超过12位，其中小数点位数不大于4位)验证
	jQuery.validator.addMethod("newNum3", function(value, element) { 
		return this.optional(element) || (/^\d{1,12}(\.\d{1,4})?$/.test(value));      
	}, "请输入数字(包括长度不能超过12位，其中小数点位数不大于4位)"); 
	// 数字(包括长度不能超过16位，其中小数点位数不大于2位)验证
	jQuery.validator.addMethod("newNum4", function(value, element) { 
		return this.optional(element) || (/^\d{1,16}(\.\d{1,2})?$/.test(value));      
	}, "请输入数字(包括长度不能超过16位，其中小数点位数不大于2位)"); 
	// 数字(包括长度不能超过16位，其中小数点位数不大于2位)验证
	jQuery.validator.addMethod("newNum5", function(value, element) { 
		return this.optional(element) || (/^\d{1,16}(\.\d{1})?$/.test(value));      
	}, "请输入数字(包括长度不能超过16位，其中小数点位数不大于1位)"); 
	
		// 数字(包括长度不能超过10位，其中小数点位数不大于2位)验证
	jQuery.validator.addMethod("newNum6", function(value, element) { 
	      var decimal1 = /^[-]{0,1}[1-9]\d{0,8}(?:\.\d{0,2})?$/;
	    var decimal2 = /^[-]{0,1}[0](?:\.\d{0,2})?$/;
		return this.optional(element) || decimal1.test(value) || decimal2.test(value);      
	}, "请输入数字(包括长度不能超过13位，其中小数点位数不大于2位)"); 
	// 数字(包括长度不能超过11位，其中小数点位数不大于5位)验证
	jQuery.validator.addMethod("newNum7", function(value, element) { 
		return this.optional(element) || (/^\d{1,11}(\.\d{1,5})?$/.test(value));      
	}, "请输入数字(包括整数位不能超过11位，小数位不大于5位)"); 
		// 数字(包括长度不能超过13位，其中小数位数不大于2位)验证
	jQuery.validator.addMethod("newNum8", function(value, element) { 
		return this.optional(element) || (/^\d{1,14}(\.\d{1,2})?$/.test(value));      
	}, "请输入数字(包括整数位不能超过14位，其中小数位数不大于2位)"); 
	//数字(包括长度不能超过11位，其中小数点位数不大于4位)验证
	jQuery.validator.addMethod("newNum9", function(value, element) { 
		return this.optional(element) || (/^\d{1,12}(\.\d{1,4})?$/.test(value));      
	}, "请输入数字(包括长度不能超过16位，其中小数位数不大于4位)"); 
	// 文件格式(xls、xlsx)验证
	jQuery.validator.addMethod("fileXls", function(value, element) { 
	  var decimal = new RegExp("^.+\.(?=EXT)(EXT)$".replace(/EXT/g, "xls,xlsx".split(/\s*,\s*/).join("|")), "gi");
		return this.optional(element) || (decimal.test(value));      
	}, "文件格式必须为xls或xlsx");
	//中文、英文字母、数字、下划线组成
	jQuery.validator.addMethod("common_1", function(value, element) { 
		return this.optional(element) || (/^[a-zA-Z0-9_\u4e00-\u9fa5]+$/.test(value));      
	}, "只能有中文、英文字母、数字、下划线组成"); 
	//正整数
	jQuery.validator.addMethod("common_2", function(value, element) { 
		return this.optional(element) || (/^\d+$/.test(value));      
	},"请输入正整数"); 
	//正整数
	jQuery.validator.addMethod("number", function(value, element) { 
		return this.optional(element) || (/^\d+$/.test(value));      
	},"请输入正整数"); 
	
})(jQuery);