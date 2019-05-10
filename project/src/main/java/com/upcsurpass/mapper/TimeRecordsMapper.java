package com.ccwihr_server.mapper;

import com.ccwihr_server.domain.TimeRecords;

public interface TimeRecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TimeRecords record);

    int insertSelective(TimeRecords record);

    TimeRecords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TimeRecords record);

    int updateByPrimaryKey(TimeRecords record);
}