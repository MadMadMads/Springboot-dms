package com.dms;

import com.dms.entity.*;
import com.dms.enums.SqlExeRecordStatusEnum;
import com.dms.service.ISqlExeService;
import com.dms.utils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author: Luo
 * @description:
 * @time: 2020/11/29 15:12
 * Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DmsTest {

    @Resource
    ISqlExeService iSqlExeService;

    @Test
    public void test() {
        System.out.println("test");
    }
    @Test
    public void test1() {
        try {
            String db = "test";
            String table = "student";
            CfDatasource datasource = CfDatasource.builder().id(1).ip("127.0.0.1").port(3306).username("root").password("123456").type(1).build();
            if (StringUtils.isNotEmpty(table) && table.indexOf(".") != -1) {
                table = table.substring(table.lastIndexOf(".") + 1);
            }
            List<TableFieldEntity> fieldList = DbUtils.getColumnNames(datasource.getId(), datasource.getIp(), datasource.getPort(), datasource.getUsername(), datasource.getPassword(), db, table, datasource.getType());

            List<TableIndexEntity> indexEntityList = DbUtils.getTableIndex(datasource.getId(), datasource.getIp(), datasource.getPort(), datasource.getUsername(), datasource.getPassword(), db, table, datasource.getType());

            TableInfoEntity tableInfoEntity = DbUtils.getTableInfo(datasource.getId(), datasource.getIp(), datasource.getPort(), datasource.getUsername(), datasource.getPassword(), db, datasource.getType(), table);
            System.out.println();
        } catch (Exception e) {
        }
    }
    @Test
    public void test2() {
        RsSqlExeRecord rsSqlExeRecord = RsSqlExeRecord.builder()
                .id(2)
                .sql_text("ALTER TABLE cf_bus_group_owners ADD COLUMN `test` VARCHAR(256) NOT NULL COMMENT 'test' AFTER `bus_group_id`;")
                .create_account("admin")
                .create_name("管理员")
                .update_time(new Date())
                .create_time(new Date())
                .datasource_id(1)
                .status(SqlExeRecordStatusEnum.RUNNING.getStatus())
                .db("kb-dms")
                .build();
        iSqlExeService.sqlExe(rsSqlExeRecord,"ALTER TABLE cf_bus_group_owners ADD COLUMN `test` VARCHAR(256) NOT NULL COMMENT 'test' AFTER `bus_group_id`;");
    }
}
