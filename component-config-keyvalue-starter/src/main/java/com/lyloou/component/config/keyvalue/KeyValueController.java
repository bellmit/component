package com.lyloou.component.config.keyvalue;

import com.lyloou.component.dto.SingleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lilou
 * @since 2021/7/15
 */
@RestController
@Api(tags = "【component】配置-键值对接口")
public class KeyValueController {

    @Autowired
    private KeyValueProperties properties;

    @ApiOperation("获取所有键值对信息")
    @GetMapping("/getAllKeyValue")
    public SingleResponse<Map<String, List<KeyValue>>> getAllKeyValue() {
        return SingleResponse.buildSuccess(properties.getType());
    }

    @ApiOperation("根据type获取键值对列表")
    @GetMapping("/getKeyValueByType")
    public SingleResponse<List<KeyValue>> getKeyValueByType(
            @RequestParam("类型") String type) {
        return SingleResponse.buildSuccess(getKeyValueList(type, properties.getType()));
    }

    private List<KeyValue> getKeyValueList(String type, Map<String, List<KeyValue>> keyValueMap) {
        if (Strings.isBlank(type)) {
            return new ArrayList<>();
        }

        if (Boolean.TRUE.equals(properties.getIgnoreCase())) {
            for (String key : keyValueMap.keySet()) {
                if (type.equalsIgnoreCase(key)) {
                    return keyValueMap.get(key);
                }
            }
        }
        return keyValueMap.getOrDefault(type, new ArrayList<>());
    }

    @ApiOperation("根据type和key获取具体的某一个value")
    @GetMapping("/getValueByTypeAndKey")
    public SingleResponse<String> getValueByTypeAndKey(
            @RequestParam("类型") String type,
            @RequestParam("键") String key) {
        final Map<String, List<KeyValue>> keyValueMap = properties.getType();
        final List<KeyValue> data = getKeyValueList(type, keyValueMap);
        if (CollectionUtils.isEmpty(data)) {
            return SingleResponse.buildSuccess(null);
        }

        final String value = data.stream()
                .filter(it -> Strings.isNotBlank(it.getKey()))
                .filter(it -> isEquals(key, it))
                .map(KeyValue::getValue)
                .findFirst().orElse(null);
        return SingleResponse.buildSuccess(value);
    }

    private boolean isEquals(String key, KeyValue it) {
        if (Boolean.TRUE.equals(properties.getIgnoreCase())) {
            return it.getKey().equalsIgnoreCase(key);
        }
        return Objects.equals(key, it.getKey());
    }
}
