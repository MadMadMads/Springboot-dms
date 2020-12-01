package com.dms.dynamicdatasource;

import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.dms.entity.DynamicSqlOptionTypeEntity;
import com.dms.enums.DatasourceTypeEnum;
import com.dms.enums.SqlSyntaxCheckResultEnum;
import org.apache.commons.lang3.StringUtils;
/**
 *
 * @description: 主要用于DQL查询限制及DMLwhere控制
 * @author: Luo
 * @time: 2020/11/30 19:22
 */
public class DynamicSqlSyntaxCheck {

    public final static Integer LIMIT_ROW_NUM = 100;

    /**
     * 检查语法 +发现语法问题 修复
     *
     * @return
     */

    public static void check(DynamicSqlOptionTypeEntity entity) {
        if (StringUtils.isNotEmpty(entity.getSql())) {
            switch (entity.getOption_type_enum()) {
                case DQL:
                    if (entity.getDatasource_type_enum() == DatasourceTypeEnum.MYSQL) {
                        if (entity.getDruidObj() instanceof SQLSelectStatement) {
                            SQLSelectQueryBlock query = ((SQLSelectQueryBlock) (((SQLSelectStatement) entity.getDruidObj()).getSelect().getQuery()));

                            if (null == query.getLimit()) {
                                entity.setSyntax_check_result_enum(SqlSyntaxCheckResultEnum.NO_LIMIT);
                                query.limit(LIMIT_ROW_NUM, 0);
                                entity.setSql(query.getParent().toString());
                            }

                        }
                    }

                    break;
                case DML:
                    if (entity.getDruidObj() instanceof SQLUpdateStatement) {

                        if (null == ((SQLUpdateStatement) entity.getDruidObj()).getWhere()) {
                            entity.setSyntax_check_result_enum(SqlSyntaxCheckResultEnum.NO_WHERE);
                        }

                    } else if (entity.getDruidObj() instanceof SQLDeleteStatement) {
                        if (null == ((SQLDeleteStatement) entity.getDruidObj()).getWhere()) {
                            entity.setSyntax_check_result_enum(SqlSyntaxCheckResultEnum.NO_WHERE);

                        }
                    }
                    break;
            }
        }
    }
}
