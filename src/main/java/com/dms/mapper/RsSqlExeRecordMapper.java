package com.dms.mapper;

import com.dms.entity.RsSqlExeRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RsSqlExeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(RsSqlExeRecord record);

    RsSqlExeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RsSqlExeRecord record);

}
