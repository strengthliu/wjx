package com.ccwihr_server.mapper;

import com.ccwihr_server.domain.Clocks;

public interface ClocksMapper {
    int deleteByPrimaryKey(Integer clockId);

    int insert(Clocks record);

    int insertSelective(Clocks record);

    Clocks selectByPrimaryKey(Integer clockId);

    int updateByPrimaryKeySelective(Clocks record);

    int updateByPrimaryKey(Clocks record);
}