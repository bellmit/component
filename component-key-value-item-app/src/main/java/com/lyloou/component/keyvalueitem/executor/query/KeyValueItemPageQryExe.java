package com.lyloou.component.keyvalueitem.executor.query;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyloou.component.dto.PageInfoHelper;
import com.lyloou.component.keyvalueitem.dto.CacheNames;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemPageQry;
import com.lyloou.component.keyvalueitem.repository.entity.KeyValueItemEntity;
import com.lyloou.component.keyvalueitem.repository.service.KeyValueItemService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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

    @Cacheable(value = CacheNames.KeyValueItemCo_PAGE_KEY, key = "#qry.itemName + '::' + #qry.itemKey+'::' + #qry.cachePageKey")
    public PageInfo<KeyValueItemCo> execute3(KeyValueItemPageQry qry) {
        final Page<Object> page = PageHelper.startPage(qry.getPageNum(), qry.getPageSize());
        if (!Strings.isEmpty(qry.getOrderBy())) {
            page.setOrderBy(qry.getOrderBy().concat(" ").concat(qry.getOrderDirection()));
        }
        page.setCount(qry.isNeedTotalCount());

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

    /**
     * 对于大数据的优化
     * 1. 先根据分页信息查id
     * 2. 再根据id，查数据
     */
    @Cacheable(value = CacheNames.KeyValueItemCo_PAGE_KEY, key = "#qry.itemName + '::' + #qry.itemKey+'::' + #qry.cachePageKey")
    public PageInfo<KeyValueItemCo> execute(KeyValueItemPageQry qry) {
        final Page<Object> page = PageHelper.startPage(qry.getPageNum(), qry.getPageSize());
        if (!Strings.isEmpty(qry.getOrderBy())) {
            page.setOrderBy(qry.getOrderBy().concat(" ").concat(qry.getOrderDirection()));
        }
        page.setCount(qry.isNeedTotalCount());

        // 先查id
        final List<Integer> idList = keyValueItemService.lambdaQuery()
                .select(KeyValueItemEntity::getId)
                .eq(qry.getItemName() != null, KeyValueItemEntity::getItemName, qry.getItemName())
                .eq(qry.getItemKey() != null, KeyValueItemEntity::getItemKey, qry.getItemKey())
                .eq(KeyValueItemEntity::getDeleted, 0)
                .list()
                .stream().map(KeyValueItemEntity::getId)
                .collect(Collectors.toList());

        // 再根据id查数据
        final List<KeyValueItemEntity> entityList = keyValueItemService.lambdaQuery()
                .in(KeyValueItemEntity::getId, idList)
                .list();

        // 转化
        final List<KeyValueItemCo> list = entityList
                .stream()
                .map(keyValueItemEntity -> {
                    final KeyValueItemCo keyValueItemCo = new KeyValueItemCo();
                    BeanUtils.copyProperties(keyValueItemEntity, keyValueItemCo);
                    return keyValueItemCo;
                })
                .collect(Collectors.toList());
        return PageInfoHelper.getPageInfo(idList, list);

    }
}
