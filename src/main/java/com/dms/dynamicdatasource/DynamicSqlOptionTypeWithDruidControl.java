package com.dms.dynamicdatasource;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.dms.entity.DynamicSqlOptionTypeEntity;
import com.dms.enums.DatasourceTypeEnum;
import com.dms.enums.SqlOptionTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @description: 大SQL拆分
 * @author: Luo
 * @time: 2020/11/30 19:22
 */
public class DynamicSqlOptionTypeWithDruidControl {

    /**
     * SQL分割 +分类
     *
     * @param sql
     * @return
     */
    public static List<DynamicSqlOptionTypeEntity> dealSqlOptionType(String sql, DatasourceTypeEnum datasourceTypeEnum) {
        List<DynamicSqlOptionTypeEntity> sqlOptionTypeEntityList = new ArrayList<>();

        if (StringUtils.isNotEmpty(sql)) {

            List<SQLStatement> smList = SQLUtils.parseStatements(sql, datasourceTypeEnum.getName());


            if (!CollectionUtils.isEmpty(smList)) {
                smList.forEach(s -> {
                    DynamicSqlOptionTypeEntity sqlOptionTypeEntity = DynamicSqlOptionTypeEntity.builder()
                            .datasource_type_enum(datasourceTypeEnum)
                            .origin_sql(s.toString())
                            .sql(s.toString())
                            .druidObj(s)
                            .build();

                    if (s instanceof SQLUpdateStatement || s instanceof SQLDeleteStatement || s instanceof SQLInsertStatement) {
                        sqlOptionTypeEntity.setOption_type_enum(SqlOptionTypeEnum.DML);

                    } else if (s instanceof SQLDDLStatement) {
                        sqlOptionTypeEntity.setOption_type_enum(SqlOptionTypeEnum.DDL);
                    } else if (s instanceof SQLSelectStatement) {
                        sqlOptionTypeEntity.setOption_type_enum(SqlOptionTypeEnum.DQL);
                    } else {
                        sqlOptionTypeEntity.setOption_type_enum(SqlOptionTypeEnum.EXEC);
                    }

                    sqlOptionTypeEntityList.add(sqlOptionTypeEntity);

                });
                return sqlOptionTypeEntityList;
            }
        }
        return sqlOptionTypeEntityList;
    }
}
