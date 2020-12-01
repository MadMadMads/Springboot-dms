package com.dms.entity;

import com.dms.enums.DatasourceTypeEnum;
import com.dms.enums.SqlOptionTypeEnum;
import com.dms.enums.SqlSyntaxCheckResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DynamicSqlOptionTypeEntity {
    private String origin_sql;

    private String sql;

    private SqlOptionTypeEnum option_type_enum;

    private DatasourceTypeEnum datasource_type_enum;

    private SqlSyntaxCheckResultEnum syntax_check_result_enum;

    private Object druidObj;

    private String table_list;
}
