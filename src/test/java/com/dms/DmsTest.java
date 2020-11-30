package com.dms;

import com.dms.entity.CfDatasource;
import com.dms.entity.TableFieldEntity;
import com.dms.entity.TableIndexEntity;
import com.dms.entity.TableInfoEntity;
import com.dms.utils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
