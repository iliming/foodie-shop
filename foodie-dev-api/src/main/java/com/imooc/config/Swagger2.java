package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//需要被容器扫描到
@Configuration
//开启swagger2配置
@EnableSwagger2
public class Swagger2 {
    // http://localhost:8088/swagger-ui.html  原路径
    // http://localhost:8088/doc.html  对swagger2文档换肤
    //使用 @ApiIgnore 注解可以隐藏不想展示的controller接口含义
    //使用 @Api 注解来解释说明controller
    //使用 @ApiOperation 来对接口中特定的方法解释
    //使用 @ApiModel 来对接口中传递的实体类进行解释说明
    //使用 @ApiModelProperty 来对实体类中的属性进行解释说明

    //配置swagger2核心配置 docket
    @Bean //只有加上@Bean以后spring才知道它是一个Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)  //指定api类型为swagger2
                .apiInfo(apiInfo())                     //用于定义api文档汇总信息
                .select().apis(RequestHandlerSelectors.basePackage("com.imooc.controller")) //指定controller包
                .paths(PathSelectors.any())   // 所有controller
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台接口API")    // 文档标题
                .contact(new Contact("imooc", "https://www.imooc.com", "abc@imooc.com"))  //联系人信息
                .description("专为天天吃货提供的API文档") //详细信息
                .version("1.0.1")  // 文档版本号
                .termsOfServiceUrl("https://www.imooc.com") //网站地址
                .build();
    }

}
