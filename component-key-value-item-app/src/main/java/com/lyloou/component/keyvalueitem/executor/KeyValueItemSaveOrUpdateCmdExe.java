package com.lyloou.component.keyvalueitem.executor;

import com.lyloou.component.keyvalueitem.dto.CacheNames;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemSaveOrUpdateCmd;
import com.lyloou.component.keyvalueitem.repository.entity.KeyValueItemEntity;
import com.lyloou.component.keyvalueitem.repository.service.KeyValueItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

/**
 * @author lilou
 * @since 2021/4/16
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyValueItemSaveOrUpdateCmdExe {

    private final KeyValueItemService keyValueItemService;

    @CacheEvict(value = CacheNames.KeyValueItemCo_NAME_KEY, key = "#cmd.keyValueItem.itemName + '::' + #cmd.keyValueItem.itemKey")
    public boolean execute(KeyValueItemSaveOrUpdateCmd cmd) {
        final KeyValueItemCo keyValueItemCo = cmd.getKeyValueItem();
        KeyValueItemEntity entity = new KeyValueItemEntity();
        BeanUtils.copyProperties(keyValueItemCo, entity);
        return keyValueItemService.saveOrUpdate(entity);
    }
}
