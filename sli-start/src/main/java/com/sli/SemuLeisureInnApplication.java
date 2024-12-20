package com.sli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author: Semu
 * @date: 2023/1/16
 * @description: 核心启动类
 */
@SpringBootApplication(scanBasePackages = {"com.sli"},exclude= {DataSourceAutoConfiguration.class})
public class SemuLeisureInnApplication {
    public static void main(String[] args) {
        SpringApplication.run(SemuLeisureInnApplication.class, args);
    }
}
