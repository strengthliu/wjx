package com.ccwihr_server.mapper;

import com.ccwihr_server.domain.Departs;

public interface DepartsMapper {
    int deleteByPrimaryKey(String departId);

    int insert(Departs record);

    int insertSelective(Departs record);

    Departs selectByPrimaryKey(String departId);

    int updateByPrimaryKeySelective(Departs record);

    int updateByPrimaryKey(Departs record);
}