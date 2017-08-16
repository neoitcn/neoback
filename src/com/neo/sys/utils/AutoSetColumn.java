package com.neo.sys.utils;

import com.neo.entity.Sys_user;
import com.neo.sys.emue.SessionAttribute;
import org.apache.shiro.SecurityUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;


public class AutoSetColumn {
    public static void autoInsert(Object obj) {
        System.out.println("自动填充uptime和createtime字段");
        Class clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        if (methods != null) {
            for (Method method : methods) {
                switch (method.getName()) {
                    case "setId":
                        Class [] clarr = method.getParameterTypes();
                        if(clarr != null && clarr.length == 1){
                            if(clarr[0].equals(String.class)){
                                try {
                                    method.invoke(obj, UUID.randomUUID().toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case "setCreaterId":
                    	Sys_user user = (Sys_user) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.CURRENT_USER);
                        if(user != null){
                            try {
                                method.invoke(obj, user.getId()==null?user.getName():user.getId());
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case "setCreateTime":
                        try {
                            method.invoke(obj, new Date());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "setUpdateId":
                        break;
                    case "setUpdateTime":
                        try {
                            method.invoke(obj, new Date());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "setDeleted":
                        try {
                            method.invoke(obj, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "setEnabled":
                        try {
                            method.invoke(obj, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }

    public static void autoUpdate(Object obj) {
        Class clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        if (methods != null) {
            for (Method method : methods) {
                switch (method.getName()) {
                    case "setUpdateId":
                    	Sys_user user = (Sys_user) SecurityUtils.getSubject().getSession().getAttribute(SessionAttribute.CURRENT_USER);
                        if(user != null){
                            try {
                                method.invoke(obj, obj, user.getId()==null?user.getName():user.getId());
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case "setUpdateTime":
                        try {
                            method.invoke(obj, new Date());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "setDeleted":
                        try {
                            Method tm = clazz.getMethod("getDeleted",new Class[]{});
                            Object o = tm.invoke(obj,new Object[]{});
                            if(o == null)
                                method.invoke(obj, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "setEnabled":
                        try {
                            Method tm = clazz.getMethod("getEnabled",new Class[]{});
                            Object o = tm.invoke(obj,new Object[]{});
                            if(o == null)
                                method.invoke(obj, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }
}
