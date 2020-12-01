package com.dms.mapper;


import com.dms.entity.CfDatasource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CfDatasourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(CfDatasource record);

    CfDatasource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CfDatasource record);

    List<CfDatasource> selectList(CfDatasource record);

    int selectCount(CfDatasource record);

}
