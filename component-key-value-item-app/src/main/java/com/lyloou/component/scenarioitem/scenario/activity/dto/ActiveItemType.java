package com.lyloou.component.scenarioitem.scenario.activity.dto;

import com.lyloou.component.scenarioitem.dto.ItemType;

/**
 * @author lilou
 */
public enum ActiveItemType implements ItemType {

    /**
     * 基础设置
     */
    common,

    /**
     * 扫码购设置
     */
    lottery,
    screeningRoom,
    recommendation,
    ;


    @Override
    public String getName() {
        return name();
    }
}
