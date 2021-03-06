package com.lyloou.component.common.dto;

import com.lyloou.component.dto.DTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the object communicate with Client.
 * The clients could be view layer or other HSF Consumers
 *
 * @author lilou
 */
public abstract class ClientObject extends DTO {

    /**
     * This is for extended values
     */
    @ApiModelProperty(value = "额外信息，map集合", hidden = true)
    protected Map<String, Object> extValues = null;

    public Object getExtField(String key) {
        if (extValues != null) {
            return extValues.get(key);
        }
        return null;
    }

    public void putExtField(String fieldName, Object value) {
        if (extValues == null) {
            extValues = new HashMap<>();
        }
        this.extValues.put(fieldName, value);
    }

    public Map<String, Object> getExtValues() {
        return extValues;
    }

    public void setExtValues(Map<String, Object> extValues) {
        this.extValues = extValues;
    }
}
