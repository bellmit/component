package com.lyloou.component.keyvalueitem.executor.query;

import com.lyloou.component.keyvalueitem.dto.CacheNames;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemForAppCo;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemGetQry;
import com.lyloou.component.keyvalueitem.repository.entity.KeyValueItemEntity;
import com.lyloou.component.keyvalueitem.repository.service.KeyValueItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author lilou
 * @since 2021/4/16
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyValueItemGetQryExe {
    private final KeyValueItemService keyValueItemService;

    @Cacheable(value = CacheNames.KeyValueItemCo_NAME_KEY, key = "#qry.itemName + '::' + #qry.itemKey")
    public KeyValueItemForAppCo execute(KeyValueItemGetQry qry) {
        return keyValueItemService.lambdaQuery()
                .eq(KeyValueItemEntity::getItemName, qry.getItemName())
                .eq(qry.getItemKey() != null, KeyValueItemEntity::getItemKey, qry.getItemKey())
                .eq(KeyValueItemEntity::getDeleted, 0)
                .oneOpt()
                .map(keyValueItemEntity -> {
                    final KeyValueItemForAppCo co = new KeyValueItemForAppCo();
                    BeanUtils.copyProperties(keyValueItemEntity, co);
                    return co;
                })
                .orElse(null);
    }
}
