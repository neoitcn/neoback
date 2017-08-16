package com.neo.sys.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Spring中的配置文件值
 */
@Component
public class ConfigServiceValues {

    @Value("${static.diy}")
    private String filePathSuff;

    @Value("${static.diy.mapper}")
    private String filePathStaticMapper;

    @Value("${static.jslib}")
    private String jslibSuff;

    @Value("${static.jslib.mapper}")
    private String jslibMapper;

    public String getFilePathSuff() {
        return filePathSuff;
    }

    public String getFilePathStaticMapper() {
        return filePathStaticMapper;
    }

    public String getJslibSuff() {
        return jslibSuff;
    }

    public String getJslibMapper() {
        return jslibMapper;
    }
}
