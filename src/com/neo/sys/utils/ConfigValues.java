package com.neo.sys.utils;

import com.neo.sys.service.ConfigServiceValues;

/**
 * Created by Shisan on 2017/2/8.
 * Spring读取的配置文件获取值。
 */
public class ConfigValues {

    private static ConfigServiceValues csv = SpringUtils.getApplicationBean(ConfigServiceValues.class);

    private static String diyFolder;

    private static String diyMapper;

    private static String commonResourceFolder;

    private static String commonResourceMapper;
    /**
     * 获取文件存放的位置，不带前缀左斜杠/，默认为user/diy/
     * @return
     */
    public static String getDiyFolderPath(){
        if(diyFolder == null){
            synchronized (ConfigValues.class){
                if(diyFolder == null){
                    String _diyFolder = csv.getFilePathSuff();
                    if(_diyFolder!=null){
                        if(_diyFolder.startsWith("/")){
                            _diyFolder = _diyFolder.substring(1);
                        }
                        if(!_diyFolder.endsWith("/")){
                            _diyFolder = _diyFolder+"/";
                        }
                    }else{
                        _diyFolder = "user/diy/";
                    }
                    diyFolder=_diyFolder;
                }
            }
        }
        return diyFolder;
    }

    public static String getDiyFolderMapper(){
        if(diyMapper == null){
            synchronized (ConfigValues.class){
                if(diyMapper == null){
                    String _diyMapper = csv.getFilePathStaticMapper();
                    if(_diyMapper!=null){
                        if(_diyMapper.startsWith("/")){
                            _diyMapper = _diyMapper.substring(1);
                        }
                        if(!_diyMapper.endsWith("/")){
                            _diyMapper = _diyMapper+"/";
                        }
                    }else{
                        _diyMapper = "diy/";
                    }
                    diyMapper=_diyMapper;
                }
            }
        }
        return diyMapper;
    }

    public static String getCommonResourceFolder(){
        if(commonResourceFolder == null){
            synchronized (ConfigValues.class){
                if(commonResourceFolder == null){
                    String _commonResourceFolder = csv.getJslibSuff();
                    if(_commonResourceFolder!=null){
                        if(_commonResourceFolder.startsWith("/")){
                            _commonResourceFolder = _commonResourceFolder.substring(1);
                        }
                        if(!_commonResourceFolder.endsWith("/")){
                            _commonResourceFolder = _commonResourceFolder+"/";
                        }
                    }else{
                        _commonResourceFolder = "common/resource";
                    }
                    commonResourceFolder=_commonResourceFolder;
                }
            }
        }
        return commonResourceFolder;
    }

    public static String getCommonResourceMapper(){
        if(commonResourceMapper == null){
            synchronized (ConfigValues.class){
                if(commonResourceMapper == null){
                    String _commonResourceMapper = csv.getJslibMapper();
                    if(_commonResourceMapper!=null){
                        if(_commonResourceMapper.startsWith("/")){
                            _commonResourceMapper = _commonResourceMapper.substring(1);
                        }
                        if(!_commonResourceMapper.endsWith("/")){
                            _commonResourceMapper = _commonResourceMapper+"/";
                        }
                    }else{
                        _commonResourceMapper = "resource";
                    }
                    commonResourceMapper=_commonResourceMapper;
                }
            }
        }
        return commonResourceMapper;
    }

}
