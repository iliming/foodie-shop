package com.imooc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

//@SpringBootApplication注解默认扫描 com.imooc 包下的所有文件 包括 service, pojo 所以这个两层也需要在com.immoc包之下，但是没有Mapper
@SpringBootApplication
//扫描mybatis通用mapper所在的包  注意这里使用的是通用mapper（tk.mybatis）
@MapperScan(basePackages = "com.imooc.mapper")
//扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
