package com.dms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CfDatasource {
    private Integer id;

    private String name;

    private String description;
    //  1. Mysql 2. Sqlserver 3. mongodb 4. Redis  5. mq
    private Integer type;

    private String ip;

    private Integer port;

    private String db;

    private String username;

    private String password;

    private String creator_name;

    private String creator_account;

    private Date create_time;

    private Date update_time;
    //  开启关闭
    private Integer query_switch;
}
