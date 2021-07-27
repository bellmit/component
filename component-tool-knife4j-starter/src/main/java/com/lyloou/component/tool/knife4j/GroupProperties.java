/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyloou.component.tool.knife4j;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Swagger配置属性
 **/
@Data
@ConfigurationProperties(prefix = "component.swagger")
public class GroupProperties {

    /**
     * 是否启用Swagger
     */
    private boolean enable = true;

    /**
     * key：组名
     * value：配置项
     */
    private Map<String, ItemProperties> items = new LinkedHashMap<String, ItemProperties>() {{
        put("default", new ItemProperties());
    }};

}
