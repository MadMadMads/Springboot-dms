package com.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: Luo
 * @description:
 * @time: 2020/11/29 14:14
 * Modified By:
 */
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class DmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DmsApplication.class, args);
    }

}
