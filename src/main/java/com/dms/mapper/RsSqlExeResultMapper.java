package com.dms.mapper;

import com.dms.entity.RsSqlExeResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RsSqlExeResultMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(RsSqlExeResult record);

    RsSqlExeResult selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RsSqlExeResult record);


    List<RsSqlExeResult> selectList(RsSqlExeResult base);

}
