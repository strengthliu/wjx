package com.ccwihr_server.mapper;

import com.ccwihr_server.domain.MealRecords;

public interface MealRecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MealRecords record);

    int insertSelective(MealRecords record);

    MealRecords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MealRecords record);

    int updateByPrimaryKey(MealRecords record);
}