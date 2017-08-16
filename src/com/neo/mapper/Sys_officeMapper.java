package com.neo.mapper;

import com.neo.entity.Sys_office;

public interface Sys_officeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sys_office record);

    int insertSelective(Sys_office record);

    Sys_office selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sys_office record);

    int updateByPrimaryKey(Sys_office record);
}