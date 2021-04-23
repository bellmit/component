package com.lyloou.component.keyvalueitem.dto;

/**
 * @author lilou
 * @since 2021/4/7
 */
public interface CacheNames {
    String KeyValueItemCo_NAME_KEY = "com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo.name";
    String KeyValueItemCo_LIST_KEY = "com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo.list";
    String KeyValueItemCo_PAGE_KEY = "com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo.page";

    static void main(String[] args) {
        System.out.println(KeyValueItemCo_NAME_KEY);
    }
}
