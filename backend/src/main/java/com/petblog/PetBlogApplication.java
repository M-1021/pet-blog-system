package com.petblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 宠物博客系统 - 主启动类
 * 使用 @SpringBootApplication 标注这是一个 Spring Boot 应用
 * 使用 @MapperScan 扫描 Mapper 接口所在的包
 */
@SpringBootApplication
@MapperScan("com.petblog.mapper")
public class PetBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetBlogApplication.class, args);
    }
}
