package com.lyloou.component.keyvalueitem.executor.query;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyloou.component.dto.PageInfoHelper;
import com.lyloou.component.keyvalueitem.dto.CacheNames;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemPageQry;
import com.lyloou.component.keyvalueitem.repository.entity.KeyValueItemEntity;
import com.lyloou.component.keyvalueitem.repository.service.KeyValueItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/4/16
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyValueItemPageQryExe {
    private final KeyValueItemService keyValueItemService;

    @Cacheable(value = CacheNames.KeyValueItemCo_PAGE_KEY, key = "#qry.itemName + '::' + #qry.itemKey+'::' + #qry.pageNo + '::'+ #qry.pageSize")
    public PageInfo<KeyValueItemCo> execute(KeyValueItemPageQry qry) {
        PageHelper.startPage(qry.getPageNo(), qry.getPageSize());

        final List<KeyValueItemEntity> entityList = keyValueItemService.lambdaQuery()
                .eq(qry.getItemName() != null, KeyValueItemEntity::getItemName, qry.getItemName())
                .eq(qry.getItemKey() != null, KeyValueItemEntity::getItemKey, qry.getItemKey())
                .eq(KeyValueItemEntity::getDeleted, 0)
                .list();

        final List<KeyValueItemCo> list = entityList
                .stream()
                .map(keyValueItemEntity -> {
                    final KeyValueItemCo keyValueItemCo = new KeyValueItemCo();
                    BeanUtils.copyProperties(keyValueItemEntity, keyValueItemCo);
                    return keyValueItemCo;
                })
                .collect(Collectors.toList());
        return PageInfoHelper.getPageInfo(entityList, list);

    }
}
