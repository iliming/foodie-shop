package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置类
 */
@Configuration //既然是配置就要加这个注解
public class WebMvcConfig {
    //构建一个restTemplate 这样在容器里就可以使用restTemplate
    @Bean  //让容器扫描到
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
