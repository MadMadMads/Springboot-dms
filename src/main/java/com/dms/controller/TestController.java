package com.dms.controller;

import com.dms.entity.*;
import com.dms.utils.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: Luo
 * @description:
 * @time: 2020/11/29 14:14
 * Modified By:
 */
@Slf4j
@Controller
public class TestController {
    @RequestMapping(value = "/datasource/{id}/{db}/{table}/_info")
    public Object ajax_get_datasource_db_table_info(@PathVariable("id") Integer id, @PathVariable("db") String db, @PathVariable("table") String table) {

        Object response = new Object();
        try {
            CfDatasource datasource = CfDatasource.builder().id(1).ip("127.0.0.1").port(3306).username("root").password("123456").type(1).build();
            if (StringUtils.isNotEmpty(table) && table.indexOf(".") != -1) {
                table = table.substring(table.lastIndexOf(".") + 1);
            }
            List<TableFieldEntity> fieldList = DbUtils.getColumnNames(datasource.getId(), datasource.getIp(), datasource.getPort(), datasource.getUsername(), datasource.getPassword(), db, table, datasource.getType());

            List<TableIndexEntity> indexEntityList = DbUtils.getTableIndex(datasource.getId(), datasource.getIp(), datasource.getPort(), datasource.getUsername(), datasource.getPassword(), db, table, datasource.getType());

            TableInfoEntity tableInfoEntity = DbUtils.getTableInfo(datasource.getId(), datasource.getIp(), datasource.getPort(), datasource.getUsername(), datasource.getPassword(), db, datasource.getType(), table);

        } catch (Exception e) {
            log.error("获取数据库表详情接口异常：" + e);
        }
        return response;
    }
}
