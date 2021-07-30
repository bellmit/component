package com.lyloou.component.tool.knife4j;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Optional;

import static springfox.documentation.schema.Annotations.findPropertyAnnotation;
import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;

/**
 * 按照类中字段顺序显示
 */
@Component
@Slf4j
public class ApiModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        try {
            Optional<BeanPropertyDefinition> beanPropertyDefinitionOptional = context.getBeanPropertyDefinition();
            Optional<ApiModelProperty> annotation = Optional.empty();
            if (context.getAnnotatedElement().isPresent()) {
                annotation = findApiModePropertyAnnotation(context.getAnnotatedElement().get());
            }
            if (context.getBeanPropertyDefinition().isPresent()) {
                annotation = findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class);
            }
            if (beanPropertyDefinitionOptional.isPresent()) {
                BeanPropertyDefinition beanPropertyDefinition = beanPropertyDefinitionOptional.get();
                if (annotation.isPresent() && annotation.get().position() != 0) {
                    return;
                }
                AnnotatedField annotatedField = beanPropertyDefinition.getField();
                if (annotatedField == null) {
                    return;
                }
                Class<?> clazz = annotatedField.getDeclaringClass();
                Field[] fields = clazz.getDeclaredFields();
                // 获取当前字段对象
                Field field = clazz.getDeclaredField(annotatedField.getName());
                boolean required = false;
                // 获取字段注解
                NotNull notNull = field.getDeclaredAnnotation(NotNull.class);
                NotBlank notBlank = field.getDeclaredAnnotation(NotBlank.class);
                if (notNull != null || notBlank != null) {
                    required = true;
                }
                int position = indexOf(fields, field);
                if (position != -1) {
                    context.getBuilder().position(position).required(required);
                }
            }
        } catch (Exception exception) {
            log.error("Swagger ApiModelProperty预处理异常", exception);
        }
    }

    public static int indexOf(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if (array == null) {
            return -1;
        } else {
            if (startIndex < 0) {
                startIndex = 0;
            }

            int i;
            if (objectToFind == null) {
                for (i = startIndex; i < array.length; ++i) {
                    if (array[i] == null) {
                        return i;
                    }
                }
            } else if (array.getClass().getComponentType().isInstance(objectToFind)) {
                for (i = startIndex; i < array.length; ++i) {
                    if (objectToFind.equals(array[i])) {
                        return i;
                    }
                }
            }

            return -1;
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
