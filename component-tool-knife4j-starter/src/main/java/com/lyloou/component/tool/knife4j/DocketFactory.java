package com.lyloou.component.tool.knife4j;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author lilou
 * @since 2021/7/27
 */
@Slf4j
public class DocketFactory {
    private static final DocketFactory INSTANCE = new DocketFactory();
    /**
     * 扫描多包时，包路径的拆分符,分号
     */
    private static final String SPLIT_COMMA = ",";

    /**
     * 扫描多包时，包路径的拆分符,逗号
     */
    private static final String SPLIT_SEMICOLON = ";";

    /**
     * Swagger忽略的参数类型
     */
    private final Class<?>[] ignoredParameterTypes = new Class[]{
            ServletRequest.class,
            ServletResponse.class,
            HttpServletRequest.class,
            HttpServletResponse.class,
            HttpSession.class,
            ApiIgnore.class
    };

    public static Docket create(SwaggerItemProperties swaggerProperties) {
        return INSTANCE.createDocket(swaggerProperties);
    }

    private Docket createDocket(SwaggerItemProperties swaggerProperties) {
        // 获取需要扫描的包
        String[] basePackages = getBasePackages(swaggerProperties);
        ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(swaggerProperties))
                .select();
        // 如果扫描的包为空，则默认扫描类上有@Api注解的类
        if (ArrayUtils.isEmpty(basePackages)) {
            apiSelectorBuilder.apis(RequestHandlerSelectors.withClassAnnotation(Api.class));
        } else {
            // 扫描指定的包
            apiSelectorBuilder.apis(basePackage(basePackages));
        }
        return apiSelectorBuilder.paths(PathSelectors.any())
                .build()
                .enable(swaggerProperties.isEnable())
                .ignoredParameterTypes(ignoredParameterTypes)
                .globalOperationParameters(getParameters(swaggerProperties))
                .groupName(swaggerProperties.getGroupName());
    }

    /**
     * 获取apiInfo
     */
    private ApiInfo apiInfo(SwaggerItemProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .termsOfServiceUrl(swaggerProperties.getUrl())
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

    /**
     * 获取扫描的包
     */
    private String[] getBasePackages(SwaggerItemProperties swaggerProperties) {
        log.info("swaggerProperties = " + swaggerProperties);
        String basePackage = swaggerProperties.getBasePackage();
        if (StringUtils.isBlank(basePackage)) {
            return null;
        }
        String[] basePackages;
        if (basePackage.contains(SPLIT_COMMA)) {
            basePackages = basePackage.split(SPLIT_COMMA);
        } else if (basePackage.contains(SPLIT_SEMICOLON)) {
            basePackages = basePackage.split(SPLIT_SEMICOLON);
        } else {
            basePackages = new String[]{basePackage};
        }
        log.info("swagger scan basePackages:" + Arrays.toString(basePackages));
        return basePackages;
    }

    /**
     * 添加额外参数
     */
    private List<Parameter> getParameters(SwaggerItemProperties swaggerProperties) {
        // 获取自定义参数配置
        List<SwaggerItemProperties.ParameterConfig> parameterConfig = swaggerProperties.getParameterConfig();
        if (CollectionUtils.isEmpty(parameterConfig)) {
            return null;
        }
        List<Parameter> parameters = new ArrayList<>();
        parameterConfig.forEach(parameter -> {
            // 设置自定义参数
            parameters.add(new ParameterBuilder()
                    .name(parameter.getName())
                    .description(parameter.getDescription())
                    .modelRef(new ModelRef(parameter.getDataType()))
                    .parameterType(parameter.getType())
                    .required(parameter.isRequired())
                    .defaultValue(parameter.getDefaultValue())
                    .build());
        });
        return parameters;
    }


    private static Predicate<RequestHandler> basePackage(final String[] basePackages) {
        return input -> declaringClass(input).map(handlerPackage(basePackages)).orElse(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final String[] basePackages) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackages) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    @SuppressWarnings("deprecation")
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }


}
