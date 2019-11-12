package com.api.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String[] ENABLE_ENV_LIST = { "test", "dev" };

    @Value("${DEPLOY_ENV}")
    private String                env;

    @Bean
    public Docket createRestApi() {
        boolean enable = Arrays.stream(ENABLE_ENV_LIST).anyMatch(t -> t.equals(env));

        return new Docket(DocumentationType.SWAGGER_2)
        		.enable(enable)
        		.apiInfo(apiInfo())
        		.groupName("数据接口入口")
        		.select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(regex("/router.*|/api.*"))
                .build();
    }
    
    @Bean
    public Docket createRestApiManage() {
    	boolean enable = Arrays.stream(ENABLE_ENV_LIST).anyMatch(t -> t.equals(env));
    	
    	return new Docket(DocumentationType.SWAGGER_2)
    			.enable(enable)
    			.apiInfo(apiInfo())
    			.groupName("后台管理")
    			.select()
    			.apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(regex("/clearCache.*|/mockSwitch.*"))
    			.build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("API文档")
        		.title("V1.0版本 API文档")
                .version("1.0")
                .build();
    }
}
