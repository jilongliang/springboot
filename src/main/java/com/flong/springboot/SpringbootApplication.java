package com.flong.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * 参考文章：https://www.cnblogs.com/qwlscn/p/11490071.html
 */
@SpringBootApplication
@ComponentScan("com.flong.springboot")  //扫描com.flong文件目录下
@MapperScan(basePackages = {"com.flong.springboot"}) //扫描DAO
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}


}
