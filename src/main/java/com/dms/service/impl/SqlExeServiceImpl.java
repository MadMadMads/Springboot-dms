package com.dms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dms.dynamicdatasource.DynamicDataExeUtils;
import com.dms.dynamicdatasource.DynamicSqlOptionTypeWithDruidControl;
import com.dms.dynamicdatasource.DynamicSqlSyntaxCheck;
import com.dms.entity.*;
import com.dms.enums.*;
import com.dms.mapper.CfDatasourceMapper;
import com.dms.mapper.RsSqlExeRecordMapper;
import com.dms.mapper.RsSqlExeResultMapper;
import com.dms.service.ISqlExeService;
import com.dms.utils.PPStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description sql执行类
 * @date 2020/11/30 19:00
 * @param
 * @return
 */
@Slf4j
@Service
public class SqlExeServiceImpl implements ISqlExeService {
    @Resource
    private RsSqlExeRecordMapper sqlExeRecordMapper;
    @Resource
    private RsSqlExeResultMapper sqlExeResultMapper;
    @Resource
    private CfDatasourceMapper datasourceMapper;

    @Override
    @Async("threadExecutor")
    public void sqlExeAsync(RsSqlExeRecord rsSqlExeRecord, String sql) {
        this.sqlExe(rsSqlExeRecord, sql);
    }


    @Override
    public void sqlExe(RsSqlExeRecord rsSqlExeRecord, String sql) {
        //  用于数据回滚
        sqlExeRecordMapper.insertSelective(rsSqlExeRecord);

        CfDatasource cfDatasource = datasourceMapper.selectByPrimaryKey(rsSqlExeRecord.getDatasource_id());

        String error_msg = null;
        //  Sql 拆分
        List<DynamicSqlOptionTypeEntity> sqlOptionTypeEntityList = new ArrayList<>();
        try {
            sqlOptionTypeEntityList = DynamicSqlOptionTypeWithDruidControl.dealSqlOptionType(sql, DatasourceTypeEnum.getByType(cfDatasource.getType()));
        } catch (Exception e) {
            error_msg = e.getMessage();
        }
        //  如果有错或者sql解析错误
        if (StringUtils.isNotEmpty(error_msg) || CollectionUtils.isEmpty(sqlOptionTypeEntityList)) {
            SqlExeResult sqlExeResult = SqlExeResult.builder()
                    .start_time(new Date())
                    .end_time(new Date())
                    .sql(sql)
                    .message(StringUtils.isNotEmpty(error_msg) ? error_msg : "请输入要执行的SQL！")
                    .success(false)
                    .build();
            sqlExeResultMapper.insertSelective(RsSqlExeResult.builder()
                    .sql_exe_record_id(rsSqlExeRecord.getId())
                    .sql_text(sql)
                    .create_time(new Date())
                    .update_time(new Date())
                    .creator_account(rsSqlExeRecord.getCreate_account())
                    .creator_name(rsSqlExeRecord.getCreate_name())
                    .status(SqlExeResultStatusEnum.FAIL.getStatus())
                    .db(rsSqlExeRecord.getDb())
                    .datasource_name(cfDatasource.getName())
                    .datasource_id(cfDatasource.getId())
                    .datasource_type(cfDatasource.getType())
                    .sql_option_type(SqlOptionTypeEnum.DQL.getType())
                    .result(JSON.toJSONString(sqlExeResult, SerializerFeature.WriteMapNullValue))
                    .build());

            sqlExeRecordMapper.updateByPrimaryKeySelective(RsSqlExeRecord.builder()
                    .status(SqlExeRecordStatusEnum.COMPLETE.getStatus())
                    .id(rsSqlExeRecord.getId())
                    .update_time(new Date())
                    .build());

            return;
        }


        sqlOptionTypeEntityList.forEach(sqlOptionTypeEntity -> {
            RsSqlExeResult rsSqlExeResult = RsSqlExeResult.builder()
                    .sql_exe_record_id(rsSqlExeRecord.getId())
                    .sql_text(sqlOptionTypeEntity.getOrigin_sql())
                    .create_time(new Date())
                    .update_time(new Date())
                    .creator_account(rsSqlExeRecord.getCreate_account())
                    .creator_name(rsSqlExeRecord.getCreate_name())
                    .status(SqlExeResultStatusEnum.RUNNING.getStatus())
                    .db(rsSqlExeRecord.getDb())
                    .datasource_name(cfDatasource.getName())
                    .datasource_id(cfDatasource.getId())
                    .datasource_type(cfDatasource.getType())
                    .sql_option_type(sqlOptionTypeEntity.getOption_type_enum().getType())
                    .table_name_list(null != PPStringUtils.getTableNames(sqlOptionTypeEntity.getSql()) ? String.join(",",PPStringUtils.getTableNames(sqlOptionTypeEntity.getSql())) : null)
                    .build();

            sqlExeResultMapper.insertSelective(rsSqlExeResult);

                //检查sql语法并修复问题
                DynamicSqlSyntaxCheck.check(sqlOptionTypeEntity);

                if (null != sqlOptionTypeEntity.getSyntax_check_result_enum()) {
                    if (sqlOptionTypeEntity.getSyntax_check_result_enum().getStop()) {
                        sqlExeResultMapper.updateByPrimaryKeySelective(RsSqlExeResult.builder()
                                .status(SqlExeResultStatusEnum.FAIL.getStatus())
                                .syntax_error_sql(sqlOptionTypeEntity.getOrigin_sql())
                                .syntax_error_type(sqlOptionTypeEntity.getSyntax_check_result_enum().getType())
                                .update_time(new Date())
                                .id(rsSqlExeResult.getId())
                                .result(JSON.toJSONString(SqlExeResult.builder().message(sqlOptionTypeEntity.getSyntax_check_result_enum().getMessage()).build(), SerializerFeature.WriteMapNullValue))
                                .build());
                    } else {
                        // DML
                        List<SqlExeResult> exeResultList = DynamicDataExeUtils.exeSql(sqlOptionTypeEntity.getSql(), cfDatasource.getId(), cfDatasource.getType(), cfDatasource.getIp(), cfDatasource.getPort(), rsSqlExeRecord.getDb(), cfDatasource.getUsername(), cfDatasource.getPassword());
                        if (!CollectionUtils.isEmpty(exeResultList)) {
                            SqlExeResult sqlExeResult = exeResultList.get(0);

                            RsSqlExeResult result = RsSqlExeResult.builder()
                                    .update_time(new Date())
                                    .status(sqlExeResult.isSuccess() ? SqlExeResultStatusEnum.SUCCESS.getStatus() : SqlExeResultStatusEnum.FAIL.getStatus())
                                    .id(rsSqlExeResult.getId())
                                    .result(JSON.toJSONString(sqlExeResult, SerializerFeature.WriteMapNullValue))
                                    .build();

                            if (sqlOptionTypeEntity.getSyntax_check_result_enum().getType().equals(SqlSyntaxCheckResultEnum.NO_LIMIT.getType())) {
                                if (null != sqlExeResult.getData() && ((List<Map<String, Object>>) sqlExeResult.getData()).size() == DynamicSqlSyntaxCheck.LIMIT_ROW_NUM) {
                                    result.setSyntax_error_sql(sqlOptionTypeEntity.getOrigin_sql());
                                    result.setSyntax_error_type(sqlOptionTypeEntity.getSyntax_check_result_enum().getType());
                                }
                            }
                            sqlExeResultMapper.updateByPrimaryKeySelective(result);
                        }


                    }
                } else {
                    List<SqlExeResult> exeResultList = DynamicDataExeUtils.exeSql(sqlOptionTypeEntity.getSql(), cfDatasource.getId(), cfDatasource.getType(), cfDatasource.getIp(), cfDatasource.getPort(), rsSqlExeRecord.getDb(), cfDatasource.getUsername(), cfDatasource.getPassword());
                    if (!CollectionUtils.isEmpty(exeResultList)) {
                        SqlExeResult sqlExeResult = exeResultList.get(0);
                        sqlExeResultMapper.updateByPrimaryKeySelective(RsSqlExeResult.builder()
                                .status(sqlExeResult.isSuccess() ? SqlExeResultStatusEnum.SUCCESS.getStatus() : SqlExeResultStatusEnum.FAIL.getStatus())
                                .update_time(new Date())
                                .id(rsSqlExeResult.getId())
                                .result(JSON.toJSONString(sqlExeResult, SerializerFeature.WriteMapNullValue))
                                .build());
                    }
                }
        });

        sqlExeRecordMapper.updateByPrimaryKeySelective(RsSqlExeRecord.builder()
                .status(SqlExeRecordStatusEnum.COMPLETE.getStatus())
                .id(rsSqlExeRecord.getId())
                .update_time(new Date())
                .build());
    }

}
