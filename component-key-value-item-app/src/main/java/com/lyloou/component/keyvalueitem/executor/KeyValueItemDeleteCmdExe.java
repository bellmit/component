package com.lyloou.component.keyvalueitem.executor;

import com.lyloou.component.keyvalueitem.dto.CacheNames;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemDeleteCmd;
import com.lyloou.component.keyvalueitem.repository.entity.KeyValueItemEntity;
import com.lyloou.component.keyvalueitem.repository.service.KeyValueItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

/**
 * @author lilou
 * @since 2021/4/16
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyValueItemDeleteCmdExe {
    private final KeyValueItemService keyValueItemService;

    @Caching(evict = {
            @CacheEvict(value = CacheNames.KeyValueItemCo_NAME_KEY, key = "#cmd.itemName + '::' + #cmd.itemKey"),
            @CacheEvict(value = CacheNames.KeyValueItemCo_PAGE_KEY, allEntries = true),
            @CacheEvict(value = CacheNames.KeyValueItemCo_LIST_KEY, allEntries = true),
    })
    public boolean execute(KeyValueItemDeleteCmd cmd) {
        return keyValueItemService.lambdaUpdate()
                .eq(KeyValueItemEntity::getItemName, cmd.getItemName())
                .eq(KeyValueItemEntity::getItemKey, cmd.getItemKey())
                .remove();
    }
}
