package com.tisu.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tisu")
@MapperScan("com.tisu.dao.mapper")
public class TiSuApplication {
    public static void main(String[] args) {
        SpringApplication.run(TiSuApplication.class, args);
    }
}
