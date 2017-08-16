package com.neo.controller.sys;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.neo.sys.exception.NormalException;
import com.neo.sys.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.neo.entity.Sys_office;
import com.neo.entity.Sys_user;
//import com.aofa.sifa.service.sys.OfficeService;
import com.neo.service.sys.UserService;
import com.neo.sys.annotation.ControllerName;
import com.neo.sys.annotation.SubToken;
import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.entity.JqGridPage;
import com.neo.sys.entity.Page;
import com.neo.sys.web.BaseController;

@Controller
@Scope(value = "prototype")
@RequestMapping("user")
@ControllerName
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
//
//    @Autowired
//    private OfficeService officeService;

    /**
     * 在用户访问登录页面的时候
     * 检查用户是否已经登录过，如果登录过，就直接跳转到首页。
     * @param request
     * @param mav
     * @return
     * @throws Exception
     */
    @ControllerName(open = false)
    @RequestMapping("login_")
    public ModelAndView loginCheck(HttpServletRequest request, ModelAndView mav) throws Exception {
        if (request.getSession().getAttribute(SessionAttribute.CURRENT_USER) == null) {
            mav.setViewName("redirect:/login.jsp");
        } else {
            mav.setViewName("redirect:/pages/index.jsp");
        }
        return mav;
    }

    @ControllerName(open = false)
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(Sys_user user, String validateCode, HttpServletRequest request) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken upt = new UsernamePasswordToken(user.getName(), user.getPassword().toCharArray());
        subject.login(upt);
        //pService.doLogin(request, user, validateCode);
        // model.setViewName("redirect:/pages/index.jsp");
        // return model;
        return SUCCESS;
    }

    @ControllerName(open = false)
    @RequestMapping(value = "login_out")
    public String loginOut(HttpServletRequest request) throws Exception {
        SecurityUtils.getSubject().logout();
        return "redirect:/login.jsp";
    }

//    @RequestMapping(value = "list")
//    @ResponseBody
//    @RequiresUser
//    @RequiresPermissions(value={"admin:r","user:r"},logical=Logical.OR)
//    public JqGridPage getUserList(String name, String loginName, Page page, HttpServletRequest request)
//            throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("name", name);
//        param.put("loginName", loginName);
//        Sys_user user = (Sys_user)request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
//        if(user.getId()!=null){
//            List<Office> listOffice = AccessUtil.helpCreateCurrentDataChildOffice();;
//            List<Integer> listIds = new ArrayList<>();
//            if(listOffice!=null){
//                for(Office o:listOffice){
//                    listIds.add(o.getId());
//                }
//            }
//            param.put("offerIds", listIds);
//            param.put("id",user.getId());
//        }
//        PageHelper.startPage(page);
//        PageHelper.setPlugParam((id,value,obj)->{
//            User u = (User)obj;
//            switch(id){
//                case "allowEdit":
//                    if(user.getId()!=null){
//                        if(user.getId().equals(u.getId())){
//                            return "1";
//                        }else{
//                            if(user.getOfficeId() == u.getOfficeId()){
//                                return "0";
//                            }else{
//                                return "1";
//                            }
//                        }
//                    }else{
//                        return "1";
//                    }
//            }
//            return null;
//        }, "id","loginName","name","userType","mobile","email","remarks","allowEdit");
//        userService.getResultsByParam(param);
//        return PageHelper.endPage();
//    }

//    @RequestMapping(value = "edit")
//    @SubToken(needSaveToken = true)
//    @RequiresPermissions(value={"admin:w","user:w"},logical = Logical.OR)
//    public ModelAndView editUser(String id, ModelAndView mav,HttpServletRequest request) throws Exception {
//        if (StringUtils.isNotBlank(id)) {
//            mav.addObject("user", userService.getResultById(id));
//            User currentUser = (User) request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
//            if(currentUser == null){ //用户没有登录，重新登录
//                mav.setViewName("redirect:/relogin.jsp");
//                return mav;
//            }else if(currentUser.getId() == null){ //用户ID为空，则为管理员
//                mav.addObject("allowEdit",true);
//                mav.addObject("allowEditAccount",true);
//            }else{
//                if(currentUser.getId().equals(id)){ //如果是用户自己修改自己，则不允许修改个人账号
//                    mav.addObject("allowEdit",true);
//                    mav.addObject("allowEditAccount",false);
//                }else{
//                    //判断当前用户是被修改的用户的上级用户，是则允许修改，不是则不允许
//                    User editUser = userService.getResultById(id);
//                    if(editUser == null){
//                        throw new NormalException("被编辑的用户不存在");
//                    }
//                    Office editOffice = editUser.getOffice();
//                    Office currentOffice = currentUser.getOffice();
//                    if(editOffice == null){
//                        throw new NormalException("用户不存在任何一个机构");
//                    }else{
//                        //查找被编辑的用户的所有父机构
//                        List<Office> listOffice = officeService.getOfficeParent(editOffice.getId());
//                        boolean accept = false;
//                        if(listOffice != null){
//                            for(Office o:listOffice){
//                                if(o.getId() == currentOffice.getId()){
//                                    accept = true;
//                                    break;
//                                }
//                            }
//                        }
//                        mav.addObject("allowEdit",accept);
//                        mav.addObject("allowEditAccount",accept);
//                    }
//                }
//            }
//        } else {
//            mav.addObject("user", new User());
//            mav.addObject("allowEdit",true);
//            mav.addObject("allowEditAccount",true);
//        }
//        mav.setViewName("sifa/user/user_edit");
//        return mav;
//    }

    @RequestMapping(value = "save")
    @ResponseBody
    @SubToken(needRemoveToken = true)
    @RequiresPermissions(value={"admin:w","user:w"},logical = Logical.OR)
    public Map<String, Object> saveUser(@Valid Sys_user user, BindingResult br,HttpServletRequest request) throws Exception {

        AccessUtil.accessValidate(br);

        //userService.saveUser(user,request);

        return SUCCESS;
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    @RequiresPermissions(value={"admin:w","user:w"},logical = Logical.OR)
    public Map<String, Object> deleteUser(@RequestParam(required = true) String id,HttpServletRequest request) throws Exception {
        userService.deleteUserById(id,request);
        return SUCCESS;
    }

    @SubToken(needSaveToken = true)
    @RequestMapping("pass_edit")
    @ControllerName(open = false)
    public ModelAndView editPassword(ModelAndView mav,HttpServletRequest request) throws Exception {
        Sys_user user = (Sys_user) request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
        if (user == null) {
            mav.setViewName("redirect:/relogin.jsp");
            return mav;
        }
        mav.setViewName("sifa/user/user_pass");
        return mav;
    }

    @SubToken(needSaveToken = true)
    @RequestMapping("info_edit")
    @ControllerName(open = false)
    public ModelAndView editInfo(ModelAndView mav, HttpServletRequest request) throws Exception {
    	Sys_user user = (Sys_user) request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
        if (user == null) {
            mav.setViewName("redirect:/relogin.jsp");
            return mav;
        }
        mav.addObject("loginName", user.getName());
        mav.addObject("name", user.getName());
        //mav.addObject("no", user.getNo());
        mav.addObject("email", user.getEmail());
//        Sys_office office = user.getOffice();
//        if (office != null) {
//            mav.addObject("officeName",office.getName());
//        }
//        mav.addObject("remark",user.getRemarks());
//        if(user.getId()==null){
//            mav.setViewName("sifa/user/admin_info");
//            return mav;
//        }
//        mav.setViewName("sifa/user/user_info");
//        return mav;
        return null;
    }

    @RequestMapping("pass")
    @SubToken(needRemoveToken = true)
    @ResponseBody
    @ControllerName(open = false)
    public Object savePassword(String oldPassword, String password, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(oldPassword)) {
            throw new Exception("请输入原始密码");
        }
        if (StringUtils.isBlank(password)) {
            throw new Exception("请输入新密码");
        }
        if (oldPassword.equals(password)) {
            throw new Exception("为了安全起见，新密码不要和原来密码一样！");
        }
        Sys_user user = (Sys_user) request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
        if (user == null) {
            throw new Exception("当前登录已过期，请重新登登录");
        }
        if(user.getId() == null){
            AdminUtil.updateAdmin(user.getName(),null,null,password);
        }else{
//            int num = userService.updateUserPassword(user.getId(), oldPassword, password);
//            if (num != 1) {
//                throw new NormalException("原密码错误");
//            }
        }
        return SUCCESS;
    }

    @RequestMapping("info")
    @SubToken(needRemoveToken = true)
    @ResponseBody
    @ControllerName(open = false)
    public Object saveInfo(HttpServletRequest request) throws Exception {
        Sys_user user = (Sys_user) request.getSession().getAttribute(SessionAttribute.CURRENT_USER);
        if (user == null) {
            throw new Exception("登录已过期，请重新登录");
        }
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String remark = request.getParameter("remark");
        user.setName(name);
        user.setEmail(email);
        user.setRemarks(remark);
        if(user.getId() == null){
            AdminUtil.updateAdmin(user.getName(),null,name,null);
        }else{
            userService.update(user);
        }
        return SUCCESS;
    }
    @RequestMapping("valdat-image")
    @ControllerName(open = false)
    public void validateImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 如果服务器已经正在生成验证码了，前台应该等待生成结果
        if (!SessionAttribute.code_creating
                .equals(request.getSession().getAttribute(SessionAttribute.IS_CODE_CREATING))) {
            try {
                request.getSession().setAttribute(SessionAttribute.IS_CODE_CREATING, SessionAttribute.code_creating);
                // request.getSession());
                response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
                response.addHeader("Cache-Control", "post-check=0, pre-check=0");
                response.setHeader("Pragma", "no-cache");
                response.setContentType("image/jpeg");
                ValidateCodeImageUtil.createRandomPutInSessionAndOutput(request.getSession(),
                        response.getOutputStream());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                request.getSession().removeAttribute(SessionAttribute.IS_CODE_CREATING);
            }
        }
    }

}
