package com.neo.sys.utils;

import com.neo.entity.Sys_office;
import com.neo.entity.Sys_user;
import com.neo.sys.emue.SessionAttribute;
import com.neo.sys.exception.NormalException;
import com.neo.sys.exception.NormalRuntimeException;
import com.neo.sys.exception.ValidateException;
import org.apache.shiro.SecurityUtils;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 断言与判断类。
 * 该类提供一些简单可行的辅助方法
 * 以access开头的方法都是断言方法，断言失败后续代码将不再执行。
 * is开头的方法都是判断方法，判断不通过则返回false。
 * helpCreate方法是创建相应参数的方法
 */
public class AccessUtil {

    /**
     * 断言数据校验是否通过
     * @param br
     */
    public static void accessValidate(BindingResult br){
        if(br.getErrorCount() > 0){
            throw new ValidateException(br);
        }
    }

    /**
     * 断言是否成功，
     * @param success
     * @param message 断言失败后返回的消息
     */
    public static void accessSuccess(boolean success,String message){
        if(!success){
            throw new NormalRuntimeException(message==null?"操作失败":message);
        }
    }

    /**
     * 停止并返回错误信息
     * @param message
     */
    public static void accessStopAndMessage(String message){
        throw new NormalRuntimeException(message);
    }

    /**
     * 选择性操作，可以在true与false之间选择性执行
     * @param success
     * @param successToDo
     * @param failerToDo
     * @param <T>
     * @return
     */
    public static <T>T confirmToDo(boolean success, Supplier<T> successToDo, Supplier<T> failerToDo){
        if(success){
            if(successToDo != null){
                return successToDo.get();
            }
        }else{
            if(failerToDo != null){
                return failerToDo.get();
            }
        }
        return null;
    }

    /**
     * 断言当前用户是否登录
     */
    public static void accessLogin(){
        if(SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.CURRENT_USER) == null){
            throw new NormalRuntimeException("您还没有登录");
        }
    }

    /**
     * 断言当前用户是否拥有机构
     */
    public static void accessHasOffice(){
        if(SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_CURRENT_OFFICE) == null){
            throw new NormalRuntimeException("当前用户不属于任何一个机构");
        }
    }

    /**
     * 断言当前用户是否是管理员
     */
    public static void accessIsAdmin(){
        if(isAdmin()){
            throw new NormalRuntimeException("管理员无权操作");
        }
    }

    /**
     * 判断当前用户是否是管理员
     * @return
     */
    public static boolean isAdmin(){
        Sys_user user = (Sys_user) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.CURRENT_USER);
        if(user == null){
            throw new NormalRuntimeException("您还没有登录");
        }
        return user.getId() == null;
    }

    /**
     *判断office是否是当前用户的Office 的子机构(包括自身)
     * @id 要判断的office 的 id。
     * @return
     */
    public static boolean isChildCurrentOffice(Integer id){
        List<Sys_office> listOffice = helpCreateCurrentDataChildOffice();
        return listOffice.stream().anyMatch(o->o.getId().equals(id));
    }

    /**
     *判断office是否是当前用户的Office 的父机构(包括自身)
     * @id 要判断的office 的 id。
     * @return
     */
    public static boolean isParentCurrentOffice(Integer id){
        List<Sys_office> listOffice = helpCreateCurrentDataParentOffice();
        return listOffice.stream().anyMatch(o->o.getId().equals(id));
    }

    /**
     * 判断office是否是当前用户的上一级机构(包括自身)
     * office中必须包含d信息
     * @param office
     * @return
     */
    public static boolean isPrevParentCurrentOffice(Sys_office office){
        List<Sys_office> listOffice = helpCreateCurrentDataParentOffice();
        return listOffice.stream().anyMatch(o->office.getId() == o.getParentId());
    }

    /**
     * 判断office是否是当前用户的下一级机构(包括自身)
     * office中必须包含parentId信息
     * @param office
     * @return
     */
    public static boolean isNextChildCurrentOffice(Sys_office office){
        List<Sys_office> listOffice = helpCreateCurrentDataParentOffice();
        return listOffice.stream().anyMatch(o->office.getParentId() == o.getId());
    }

    /**
     * 判断office是否是当前用户的同一级机构
     * office中必须包含parentId信息
     * @param office
     * @return
     */
    public static boolean isSameCurrentOffice(Sys_office office){
        List<Sys_office> listOffice = helpCreateCurrentDataParentOffice();
        return listOffice.stream().anyMatch(o->office.getParentId() == o.getParentId());
    }

    public static Sys_office helpCreateCurrentOffice(){
        return (Sys_office) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_CURRENT_OFFICE);
    }

    /**
     * 获取当前用户的office的直接上一级机构
     * 当前用户必须属于某一个机构，否则返回空。
     * @return
     */
    public static Sys_office helpCreateDataPrevParentOffice(){
    	Sys_office office = (Sys_office) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_CURRENT_OFFICE);
        if(office == null){
            return null;
        }
        List<Sys_office> listOffice = (List<Sys_office>) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_PARENT_OFFICE);
        return listOffice.stream().filter(o->o.getId()==office.getParentId()).findFirst().get();
    }

    /**
     * 获取当前用户的office的直接下一级机构
     * 当前用户必须属于某一个机构，否则返回空。
     * @return
     */
    public static List<Sys_office> helpCreateDataNextChildOffice(){
    	Sys_office office = (Sys_office) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_PARENT_OFFICE);
        if(office == null){
            return null;
        }
        List<Sys_office> listOffice = (List<Sys_office>) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_PARENT_OFFICE);
        return listOffice.stream().filter(o->o.getParentId()==office.getId()).collect(Collectors.toList());
    }
    /**
     * 获取当前用户的所有子机构,不包含自身
     * 可以从session中获取SessionAttributes.USER_CHILD_OFFICE获取
     * 该方法不会返回空值
     * @return
     */
    public static List<Sys_office> helpCreateCurrentDataChildOfficeWithoutOwn(){
        List<Sys_office> listOffice = (List<Sys_office>) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_CHILD_OFFICE);
        List<Sys_office> returnOffice = new ArrayList<>();
        if(listOffice != null){
            returnOffice.addAll(listOffice);
        }
        return returnOffice;
    }
    /**
     * 获取当前用户的机构和他的所有子机构
     * 可以从session中获取SessionAttributes.USER_CHILD_OFFICE获取
     * 该方法不会返回空值
     * @return
     */
    public static List<Sys_office> helpCreateCurrentDataChildOffice(){
        List<Sys_office> listOffice = (List<Sys_office>) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_CHILD_OFFICE);
        List<Sys_office> returnOffice = new ArrayList<>();
        if(listOffice != null){
            returnOffice.addAll(listOffice);
        }
        Sys_office office = (Sys_office) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_CURRENT_OFFICE);
        if(office != null){
            returnOffice.add(office);
        }
        return returnOffice;
    }

    /**
     * 获取当前用户的机构和他的所有父机构
     * 可以从session中获取SessionAttributes.USER_PARENT_OFFICE获取
     * 该方法不会返回空值
     * @return
     */
    public static List<Sys_office> helpCreateCurrentDataParentOffice(){
        List<Sys_office> listOffice = (List<Sys_office>) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_PARENT_OFFICE);
        List<Sys_office> returnOffice = new ArrayList<>();
        if(listOffice != null){
            returnOffice.addAll(listOffice);
        }
        Sys_office office = (Sys_office) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_CURRENT_OFFICE);
        if(office != null){
            returnOffice.add(office);
        }
        return returnOffice;
    }

    /**
     * 获取当前用户的所有父机构,不包含自身
     * 可以从session中获取SessionAttributes.USER_CHILD_OFFICE获取
     * 该方法不会返回空值
     * @return
     */
    public static List<Sys_office> helpCreateCurrentDataParentOfficeWithoutOwn(){
        List<Sys_office> listOffice = (List<Sys_office>) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.USER_PARENT_OFFICE);
        List<Sys_office> returnOffice = new ArrayList<>();
        if(listOffice != null){
            returnOffice.addAll(listOffice);
        }
        return returnOffice;
    }


}

