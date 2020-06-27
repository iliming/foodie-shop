package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 */
@Configuration //既然是配置就要加这个注解
public class WebMvcConfig implements WebMvcConfigurer {
    //构建一个restTemplate 这样在容器里就可以使用restTemplate
    @Bean  //让容器扫描到
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    //实现静态资源的映射 （使图片，音频，视频能在浏览器上访问）
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/") //映射swagger2
                .addResourceLocations("file:/META-INF/resources/");//映射本地静态资源
    }
}
