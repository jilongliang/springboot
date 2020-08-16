package com.flong.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * SpringBoot & MyBatis Plus 整合
 * 参考文章：https://www.cnblogs.com/qwlscn/p/11490071.html
 * @Author liangjl
 * @Version V1.0
 * @Copyright (c) All Rights Reserved, .
 */
@SpringBootApplication
@ComponentScan("com.flong.springboot")
@MapperScan(basePackages = {"com.flong.springboot"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
