package com.lyloou.component.tool.swagger2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author lilou
 * @since 2021/7/14
 */
@Configuration
@EnableConfigurationProperties({Swagger2Properties.class})
@ConditionalOnExpression(value = "${swagger2.enable:true}")
@EnableSwagger2WebMvc
public class Swagger2AutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket buildDocket(Swagger2Properties swagger2Properties) {
        List<String> basePackages = swagger2Properties.getBasePackages();
        // [Support multiple apis() in swagger config](https://github.com/springfox/springfox/issues/1559#issuecomment-259010468)
        Predicate<RequestHandler> selector;
        if (CollectionUtils.isEmpty(basePackages)) {
            selector = defaultSelector();
        } else {
            selector = basePackages.stream()
                    .map(RequestHandlerSelectors::basePackage)
                    .reduce(Predicate::or)
                    .orElse(null);
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey(swagger2Properties)))
                .select()
                .apis(selector)
                .paths(PathSelectors.any())
                // 关闭错误
                .paths(PathSelectors.regex("/error.*").negate())
                .build()
                .globalOperationParameters(buildParameter(swagger2Properties))
                .consumes(CONSUMES)
                ;

    }

    private Predicate<RequestHandler> defaultSelector() {
        return Stream.of(RestController.class, Controller.class)
                .map(RequestHandlerSelectors::withClassAnnotation)
                .reduce(Predicate::or).get();
    }

    public static final HashSet<String> CONSUMES = new HashSet<String>() {{
        add("application/json");
        add("application/x-www-form-urlencoded");
    }};

    private ApiInfo buildApiInf() {
        return new ApiInfoBuilder()
                .title("API文档")
                .contact(new Contact("example", "https://www.example.com", "example@email.com"))
                .version("1.0")
                .build();
    }

    private ApiKey apiKey(Swagger2Properties swagger2Properties) {
        return new ApiKey("JWT", swagger2Properties.getAuthorizationKey(), "header");
    }

    /**
     * 构建认证token参数
     *
     * @param swagger2Properties swagger2配置参数
     * @return token参数
     */
    protected List<Parameter> buildParameter(Swagger2Properties swagger2Properties) {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name(swagger2Properties.getAuthorizationKey())
                .description("令牌(Authorization)")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(tokenPar.build());

        Parameter headerParam = new ParameterBuilder()
                .name("Content-Type")
                .description("请求类型")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        pars.add(headerParam);

        return pars;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }
}
