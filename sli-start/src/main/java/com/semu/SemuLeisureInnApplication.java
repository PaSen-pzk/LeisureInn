package com.semu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: Semu
 * @date: 2023/1/16
 * @description: 核心启动类
 */
@SpringBootApplication(scanBasePackages = "com.semu")
public class SemuLeisureInnApplication {
    public static void main(String[] args) {
        SpringApplication.run(SemuLeisureInnApplication.class, args);
    }
}
