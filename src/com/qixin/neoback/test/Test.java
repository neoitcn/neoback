/*package com.neo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.neo.biz.AdminBiz;
import com.qixin.neoback.entity.Edu_news;

@Controller
@RequestMapping(value = "admin")
@SessionAttributes("")
public class AdminController {
	@Resource(name = "adminBiz")
	private AdminBiz adminBiz;

	// 1.上传新闻
	@RequestMapping(value = "/uploadNews")
	public String uploadNews(Edu_news news, String[] pic, String content,
			@RequestParam(value = "smallpicture") MultipartFile smallpicture, Map<String, Object> map,
			HttpServletRequest request) {

		try {
			// -----------------1.上传新闻缩略�?
			// 若标题图片为空则返回
			if (smallpicture.getOriginalFilename().equals("")) {
				request.setAttribute("msg", "请�?�择新闻标题图片..");
				return "main";
			}

			String filename = smallpicture.getOriginalFilename();// 获取缩略图名�?
			int index = filename.lastIndexOf(".");
			String filetype = filename.substring(index + 1);// 获得图片后缀�?
			System.out.println("图片后缀:" + filetype);
			System.out.println("图片名称:" + smallpicture.getOriginalFilename());
			System.out.println("图片smallpicture:" + smallpicture);
			System.out.println("上传图片的类�?:" + smallpicture.getContentType());

			// 定义新闻缩略图上传类�?
			List<String> typelist = new ArrayList<String>();
			typelist.add("JPG");
			typelist.add("jpg");
			typelist.add("GIF");
			typelist.add("gif");
			typelist.add("BMP");
			typelist.add("bmp");
			typelist.add("PNG");
			typelist.add("png");
			//图片类型不对则返�?..
			if (!typelist.contains(filetype)) {
				request.setAttribute("msg", "上传文件类型不对..");
				return "main";
			}
			
			long lon = new Date().getTime();//利用当前日期作为 缩略图名�? (也用做下面新闻地�?)
			System.out.println("lon1:" + lon);
			//设定图片保存的路径path
			String path = "E:/github/neoback/WebContent/news/images/titleimages/";
             // 保存文件(两种:1.通过流读取和写入,2.通过MultipartFile的transferTo保存)
			String picpath = path + lon + "." + filetype;
			smallpicture.transferTo(new File(picpath));
			System.out.println("图片上传成功.......");
			request.setAttribute("msg", "图片上传成功1..");
			
			
// ----------------------2.以下是添加新�?-----------------------------------------
			System.out.println("content:" + content);
			news.setCreateTime(new Date());//设定新闻创建时间
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			
			//加上HTML头和�? ,生成HTML页面;
			String htmlcontent;
		//2.1添加HTML头部
			htmlcontent ="<!DOCTYPE html>"
					+"<html lang=\"en\" xmlns=\"http://www.w3.org/1999/html\">"
					+"<head>"
					+"<meta charset=\"utf-8\">"
					+"<title>启芯教育</title>"
					+"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
					+"<meta name=\"description\" content=\"\" />"
					+"<meta name=\"author\" content=\"http://www.cssmoban.com\" />"
					+"<!-- css -->"
					+"<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\" />"
					+"<link href=\"css/fancybox/jquery.fancybox.css\" rel=\"stylesheet\">"
					+"<link href=\"css/jcarousel.css\" rel=\"stylesheet\" />"
					+"<link href=\"css/flexslider.css\" rel=\"stylesheet\" />"
					+"<link href=\"css/style.css\" rel=\"stylesheet\" />"
					+"<!-- Theme skin -->"
					+"<link href=\"skins/default.css\" rel=\"stylesheet\" />"
					+"<style type=\"text/css\">"
					+"<!--"
					+".STYLE2 {color: #333333}"
					+".STYLE3 {color: #FFFFFF}"
					+"-->"
					+"#con img{"
				    +" max-width:560px;"
			        +" height:auto;"
			        +" }"
					
					+"</style>"
					
					
					
					+"</head>"
					+"<div id=\"wrapper\">"
						+"<!-- start header -->"
						+"<header>"
					       +" <div class=\"navbar navbar-default navbar-static-top\">"
					           + "<div class=\"container\">"
					               + "<div class=\"navbar-header\">"
					                  + " <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">"
					                      + " <span class=\"icon-bar\"></span>"
					                      + " <span class=\"icon-bar\"></span>"
					                       +" <span class=\"icon-bar\"></span>"
					                    +"</button>"
					                   +" <a class=\"navbar-brand\" href=\"index.html\"><img src=\"img/1.png.png\" alt=\"\"/></a>"
					                +"</div>"
					                +"<div class=\"navbar-collapse collapse \">"
					                   +"<ul class=\"nav navbar-nav\">"
					                        +"<li><a href=\"index.html\">首页</a></li>"
					                        +"<li class=\"dropdown \">"
					                            +"<a href=\"#\" class=\"dropdown-toggle \" data-toggle=\"dropdown\" data-hover=\"dropdown\" data-delay=\"0\" data-close-others=\"false\">课程介绍<b class=\" icon-angle-down\"></b></a>"
					                           +" <ul class=\"dropdown-menu\">"
					                                +"<li><a href=\"dsj.html\">大数�?</a></li>"
					                                +"<li><a href=\"sjcd.html\">交互视觉设计</a></li>"
													+"<li><a href=\"vr.html\">VR</a></li>"
					                           +"</ul>"
					                        +"</li>"
					                        +"<li><a href=\"sztd.html\">师资团队</a></li>"
					                        +"<li><a href=\"xyjy.html\">学员就业</a></li>"
					                        +"<li><a href=\"gywm.html\">关于我们</a></li>"
					                    +"</ul>"
					                +"</div>"
					            +"</div>"
					        +"</div>"
						+"</header>"
						+"<!-- end header -->"
						+"<section id=\"inner-headline\">"
						+"<div class=\"container\">"
							+"<div class=\"row\">"
								+"<div class=\"col-lg-12\">"
									+"<ul class=\"breadcrumb\">"
										+"<li><a href=\"index.html\"><i class=\"fa fa-home\"></i></a><i class=\"icon-angle-right\"></i></li>"
										+"<li class=\"active\">新闻</li>"
									+"</ul>"
								+"</div>"
							+"</div>"
						+"</div>"
						+"</section>"
						+"<section id=\"content\">"
						+"<section class=\"container\">"
							+"<div class=\"row\">"
								+"<div class=\"col-lg-8\">"
									+"<article>"
									  +"<div class=\"post-image\">"
					+"<div class=\"post-heading\">"
													+"<h3 align=\"center\">"+ news.getTitle()+"</h3>"   //文章标题
									    +"</div>"

										 +" <div class=\"div_wrap\" align=\"center\">"
											  +"<ul class=\"detail_ul clearfix f3\" style=\"display: inline-block;margin-top: 18px;margin-bottom: 26px;font-size: 1.2rem;list-style: none;text-indent: 0;list-style-position: outside;\">"
												 +" <li style=\"display: inline-block;float: left;padding: 0 14px;\">时间�?<span>"+sdf.format(news.getCreateTime())+"</span>" //发布时间
												  +"</li>"
												 + "<li style=\"display: inline-block;float: left;padding: 0 14px\">发布�?<span><a href=\"http://www.neoit.cn/\">IT培训</a></span>"
												  +"</li>"
												  +"<li style=\"display: inline-block;float: left;padding: 0 14px\">来源�?<span><a href=\"http://www.tedu.cn/news/\">新闻</a></span>"
												  +"</li>"
												+ " <li style=\"display: inline-block;float: left;padding: 0 14px\" class=\"hidden-xs\"><span class=\"inline_block pull-left\">分享到：</span>"
					                              + " <span class=\"share_wrap inline_block pull-left\">"
					                                  +" <div class=\"bdsharebuttonbox bdshare-button-style0-16\" data-bd-bind=\"1502439907092\"><a href=\"#\" class=\"bds_more\" data-cmd=\"more\"></a><a title=\"分享到QQ空间\" href=\"#\" class=\"bds_qzone\" data-cmd=\"qzone\"></a><a title=\"分享到新浪微博\" href=\"#\" class=\"bds_tsina\" data-cmd=\"tsina\"></a><a title=\"分享到腾讯微博\" href=\"#\" class=\"bds_tqq\" data-cmd=\"tqq\"></a><a title=\"分享到人人网\" href=\"#\" class=\"bds_renren\" data-cmd=\"renren\"></a><a title=\"分享到微信\" href=\"#\" class=\"bds_weixin\" data-cmd=\"weixin\"></a></div>"
					+"<script>"
						+"window._bd_share_config={\"common\":{\"bdSnsKey\":{},\"bdText\":\"\",\"bdMini\":\"2\",\"bdMiniList\":false,\"bdPic\":\"\",\"bdStyle\":\"0\",\"bdSize\":\"16\"},\"share\":{}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];</script>"
					                                +"</span>"
												  +"</li>"
											  +"</ul>"
										  +"</div>"
										  +"<hr class=\"line_style\" style=\"border-top: solid 1px;box-sizing: content-box\">"
					          +"</div>"
                   +"<div id=\"con\">"
					+content   //添加新闻内容
				   +"</div>"
					
					 + "</article>"

									+"<div class=\"f1 c2 article_list article_list1\" style=\"font-size: 16px\"><span>上一篇：</span><a class=\"c2\" href=\"http://www.tedu.cn/news/234058.html\">达内教育集团荣膺北京商报在线教育中国十大品牌</a></div>"
									+"<div class=\"f1 c2 article_list\" style=\"font-size: 16px\"><span>下一篇：</span><a class=\"c2\" href=\"http://www.tedu.cn/news/242692.html\">没有下一篇了</a></div>"

									+"<div style=\"margin-top: 20px;font-size: 16px\">"
										+"<span><i class=\"fa fa-desktop\"></i>&nbsp;&nbsp;相关推荐</span>"
									+"</div>"
									+"<hr/>"
									+"<div class=\"row tui_jie\">"

										+"<div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-12\">"
											+"<dl class=\"dl-horizontal dl_list dl_detail\">"
												+"<dt><img src=\"img/tuijian01.png\" style=\"width: 138px;height: 125px\" alt=\"\" class=\"img_hover2\"></dt>"
												+"<dd>"
													+"<h3 class=\"f4\" style=\"padding-top: 0px;font-size: 1.6rem;line-height: 1.4;max-height: 44px\"><a class=\"c5\" style=\"color: #000;text-decoration: none\" href=\"http://www.tedu.cn/news/242692.html\">工信部教育与考试中心谭志彬处长参观�?�访问达内教育集�?</a></h3>"
													+"<p class=\"f2_1 c2 hidden-xs hidden-sm\" style=\"max-height: 42px;line-height: 1.5;margin-top: 10px;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 1.4rem;margin: 0px\">为迎合国家及部委的要求，贯彻落实中国制�??2025，加快行业人才培养，选拔行业高技能人才，7�?25日，工业和信息化部教育与考试中心培训处处长谭志彬、工业和信息化部教育与�?�试中心培训处项目主管龚雨涵来达内参观访问，谭处长参观了达内中关村中心的机房和教室，并了解了达内O2O人才培养模式。参观后，谭处长对达内的O2O人才培养模式表示赞赏和认可，双方就共同承办全国机器人竞赛，以及共同制定行业人才培养标准达成了战略合作意向�?</p>"
													+"<!--div class=\"clearfix f3\"><span class=\"yd_right\"><em class=\"list_icon list_time\"></em>07-28</span--><!--<span class=\"inline_block hidden-xs hidden-sm\"><em class=\"list_icon list_share\"></em>分享</span>--><!--/div-->"
												+"</dd>"
											+"</dl>"
										+"</div>"

										+"<div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-12\">"
											+"<dl class=\"dl-horizontal dl_list dl_detail\">"
												+"<dt><img src=\"img/tuijian02.jpg\" style=\"width: 138px;height: 125px\" alt=\"\" class=\"img_hover2\"></dt>"
												+"<dd>"
													+"<h3 class=\"f4\" style=\"padding-top: 0px;font-size: 1.6rem;line-height: 1.4;max-height: 44px\"><a class=\"c5\" style=\"color: #000;text-decoration: none\" href=\"http://www.tedu.cn/news/233430.html\">达内教育集团荣获得工信部“优�?培训基地”称�?</a></h3>"
													+"<p class=\"f2_1 c2 hidden-xs hidden-sm\" style=\"max-height: 42px;line-height: 1.5;margin-top: 10px;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 1.4rem;margin: 0px\">达内教育集团凭�?�优�?的教学质量�?�良好的学员口碑、以及高质量的学员就业荣获工业和信息化部教育与�?�试中心优秀培训基地�?</p>"
													+"<!--div class=\"clearfix f3\"><span class=\"yd_right\"><em class=\"list_icon list_time\"></em>07-03</span--><!--<span class=\"inline_block hidden-xs hidden-sm\"><em class=\"list_icon list_share\"></em>分享</span>--><!--/div-->"
												+"</dd>"
											+"</dl>"
										+"</div>"

										+"<div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-12\">"
											+"<dl class=\"dl-horizontal dl_list dl_detail\">"
												+"<dt><img src=\"img/tuijian03.png\" style=\"width: 138px;height: 125px\" alt=\"\" class=\"img_hover2\"></dt>"
												+"<dd>"
													+"<h3 class=\"f4\" style=\"padding-top: 0px;font-size: 1.6rem;line-height: 1.4;max-height: 44px\"><a class=\"c5\" style=\"color: #000;text-decoration: none\" href=\"http://www.tedu.cn/news/231536.html\">达内机器人课程报名火爆，家长关注孩子的动手能�?</a></h3>"
													+"<p"
					                          + "class=\"f2_1 c2 hidden-xs hidden-sm\" style=\"max-height: 42px;line-height: 1.5;margin-top: 10px;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 1.4rem;margin: 0px\">记�?�走访了京城著名STEAM教育机构达内童程童美望京旗舰中心，还没有走进达内童程童美教学区，校区的门口挂�?�?块赫然醒目�?�让未来世界科技之星在中国诞生�?�背景板立刻吸引了记者的眼球</p>"
												
												+"</dd>"
											+"</dl>"
										+"</div>"

										+"<div class=\"col-lg-6 col-md-6 col-sm-6 col-xs-12\">"
											+"<dl class=\"dl-horizontal dl_list dl_detail\">"
												+"<dt><img src=\"img/tuijian04.jpg\"  style=\"width: 138px;height: 125px\" alt=\"\" class=\"img_hover2\"></dt>"
												+"<dd>"
													+"<h3 class=\"f4\" style=\"padding-top: 0px;font-size: 1.6rem;line-height: 1.4;max-height: 44px\"><a class=\"c5\" style=\"color: #000;text-decoration: none\" href=\"http://www.tedu.cn/news/227784.html\">2017世界青少年机器人奥林匹克竞赛�?战，达内获得WRO直�?�赛承办�?</a></h3>"
													+"<p class=\"f2_1 c2 hidden-xs hidden-sm\" style=\"max-height: 42px;line-height: 1.5;margin-top: 10px;overflow: hidden;text-overflow: ellipsis;color: #666;font-size: 1.4rem;margin: 0px\">为积极响应国家�?�创新�?�战略方案，培养高素质科�?人才，推动机器人教育发展，提高学生的逻辑思维能力和科�?创新能力，达内教育集团�?�达内教育集团旗下子品牌“童程童美�?�获得WRO组委会授权直选赛承办资格</p>"
													+"<!--div class=\"clearfix f3\"><span class=\"yd_right\"><em class=\"list_icon list_time\"></em>06-12</span--><!--<span class=\"inline_block hidden-xs hidden-sm\"><em class=\"list_icon list_share\"></em>分享</span>--><!--/div-->"
												+"</dd>"
											+"</dl>"
										+"</div>"
									+"</div>"
							 +" </div>"
								+"<div class=\"col-lg-4\">"
								+	"<aside class=\"right-sidebar\">"
									+"<div class=\"widget\">"
									+	 "<div align=\"center\" style=\"margin-top: 20px\"><img src=\"img/images/1495458603018.jpg\" width=\"300\" height=\"142\"><img src=\"img/images/1495458821650.jpg\" width=\"300\" height=\"142\"><img src=\"img/images/1496737033250.jpg\" width=\"300\" height=\"142\"> </div>"
									+"<div class=\"widget\">"
										+"<h3 class=\"widgetheading\" style=\"padding-left: 8px;height: 22px;line-height: 18px;margin-top: 25px;margin-bottom: 20px;color: #000;font-size: 1.8rem\">丨开班时�?</h3>"
										+"<ul class=\"cat\">"
										 +  " <li><span style=\"font-size: 1.4rem;color: #000\">大数据开�?</span> <span style=\"display: inline-block;margin: auto;color: #000;position: absolute;left: 40%\">8�?8�? </span><a href=\"#\" style=\"height: 20px;line-height: 20px;padding: 0 15px;background: #f15b1a;position: absolute;right: 0;color: #fff;font-size: 1.4rem; text-decoration: none\">立即报名</a></li>"
										+	"<li><span style=\"font-size: 1.4rem;color: #000\">交互视觉设计</span> <span style=\"display: inline-block;margin: auto;color: #000;position: absolute;left: 40%\">8�?18�? </span><a href=\"#\" style=\"height: 20px;line-height: 20px;padding: 0 15px;background: #f15b1a;position: absolute;right: 0;color: #fff;font-size: 1.4rem; text-decoration: none\">立即报名</a></li>"
											+"<li><span style=\"font-size: 1.4rem;color: #000\">VR/AR�?�?</span> <span style=\"display: inline-block;margin: auto;color: #000;position: absolute;left: 40%\">8�?28�? </span><a href=\"#\" style=\"height: 20px;line-height: 20px;padding: 0 15px;background: #f15b1a;position: absolute;right: 0;color: #fff;font-size: 1.4rem; text-decoration: none\">立即报名</a></li>"
										+"</ul>"
									+"</div>"
								
								+"</div>"
							+"</div>"
						+"</div>"
						+" </section>"
						+" 	<footer>"
						+" 	<div class=\"container\">"
						+" <div class=\"row\">"
						+" 			<div class=\"col-lg-3\">"
						+" 					<div class=\"widget\">"
						+" 						<h5 align=\"center\" class=\"widgetheading\">关于我们</h5>"


			            +" 						<div align=\"center\">"

										         +" <address>"
											     +" <span class=\"STYLE1\"><b style=\"font-size: 15px\">专业打�?�互联网（IT）精英人�?</b><br>"
												+"地址：北京市海淀区杏石口�?98号院（北京师范大学培训基地）	   </span>"
										        + " </address>"
												+"</div>"
												+"<div align=\"center\" class=\"STYLE3\">"
													+"电话：办公室�?010-59460819</br>"
													+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
													+"王�?�师�?15910865224<br>"
													+"邮箱：qixin@neoit.cn	</div>"
											+"</div>"
										+"</div>"
										+"<div class=\"col-lg-3\">"
											+"<div class=\"widget\">"
												+"<h5 align=\"center\" class=\"widgetheading\">课程介绍</h5>"
												+"<div align=\"center\">"
													+"<ul class=\"link-list\">"
														+"<li><a href=\"dsj.html\">大数据开�?</a></li>"
														+"<li><a href=\"sjcd.html\">交互视觉设计</a></li>"
														+"<li><a href=\"vr.html\">VR/AR�?�?</a></li>"
													+"</ul>"
												+"</div>"
											+"</div>"
										+"</div>"

										+"<div class=\"col-lg-3\">"
											+"<div class=\"widget\">"
												+"<h5 align=\"center\" class=\"widgetheading\">服务专区</h5>"

												+"<div align=\"center\">"
													+"<ul class=\"link-list\">"
														+"<li><a href=\"#\">我要报名</a></li>"
														+"<li><a href=\"#\">付款方式</a></li>"
														+"<li><a href=\"#\">我要贷款</a></li>"
														+"<li><a href=\"#\">乘车路线</a></li>"
													+"</ul>"
												+"</div>"
											+"</div>"
										+"</div>"

										+"<div class=\"col-lg-3\">"
											+"<div class=\"widget\">"
												+"<h5 align=\"center\" class=\"widgetheading\">官方微信</h5>"

												+"<div class=\"footer-ewm fl_left\" align=\"center\">"

													+"<div class=\"fl_left erweima\">"
														+"<img src=\"img/images/erweima1.jpeg\" width=\"110\" height=\"110\" style=\"display: inline\" alt=\"二维码\"/>"
														+"<br/>"
														+"<p class=\"STYLE3\">扫一扫，关注我们</p>"
													+"</div>"
												+"</div>"

											+"</div>"
										+"</div>"
										+"<div class=\"flickr_badge\"></div>"
										+"<div class=\"clear\"></div>"
									+"</div>"
								+"</div>"
						+"</div>"
					+"</div>"

						+"<div style=\"cursor: default;width: 100%;height: 100%\">"
							+"<div style=\"line-height: 1.5;\">"
								+"<p align=\"center\" style=\"text-align: center;margin-top: 60px\">"
									+"<span>版权�?有�? 北京锦绣致远科技责任有限公司</span>"
								+"</p>"
							+"</div>"
						+"</div>"

						+"</footer>"
					+"</div>"
						+"<div id=\"sub-footer\"></div>"
					+"</div>"
					+"<div align=\"center\"><a href=\"#\" class=\"scrollup\"><i class=\"fa fa-angle-up active\"></i></a>"

					+"</div>"
					+"</div>"
					+"<!-- javascript"
					    +"================================================== -->"
					+"<!-- Placed at the end of the document so the pages load faster -->"
					+"<script src=\"js/jquery.js\"></script>"
					+"<script src=\"js/jquery.easing.1.3.js\"></script>"
					+"<script src=\"js/bootstrap.min.js\"></script>"
					+"<script src=\"js/jquery.fancybox.pack.js\"></script>"
					+"<script src=\"js/jquery.fancybox-media.js\"></script>"
					+"<script src=\"js/google-code-prettify/prettify.js\"></script>"
					+"<script src=\"js/portfolio/jquery.quicksand.js\"></script>"
					+"<script src=\"js/portfolio/setting.js\"></script>"
					+"<script src=\"js/jquery.flexslider.js\"></script>"
					+"<script src=\"js/animate.js\"></script>"
					+"<script src=\"js/custom.js\"></script>"

					+"</body>"
					+"</html>";

//到此HTML新闻尾添加结�?---
      
			System.out.println("lon2:" + lon);
			File file = new File("E:/github/neoback/WebContent/news/" + news.getType() + "/" + lon + ".html");
			if (!file.exists()) {
				file.createNewFile();
				if (!file.createNewFile()){
					System.out.println("news下面创建了新文件...");
				}
			}
			// 通过字符流写入到指定文件�?
			FileOutputStream out = new FileOutputStream(file, true);
			StringBuffer sb = new StringBuffer();
			sb.append(htmlcontent);
			out.write(sb.toString().getBytes("utf-8"));
			out.close();
			map.put("content", htmlcontent);
			
			//----------3.补充news的一些数�?  
		
			
			news.setHtmlUrl(lon+".html");
			news.setTitleImage(lon+"."+filetype);
			news.setEnabled(0);
			news.setDeleted(0);
			 //添加新闻
			adminBiz.insertSelective(news);
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Done");

		return "main";
	}

}
*/