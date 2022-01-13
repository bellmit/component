package com.lyloou.component.keyvalueitem.executor.query;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyloou.component.dto.ConvertUtils;
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
import org.springframework.util.CollectionUtils;

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
        if (!StrUtil.isEmpty(qry.getOrderBy())) {
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

        final Page<Object> page = PageHelper.startPage(qry.getPageNum(), qry.getPageSize(), qry.isNeedTotalCount(), false, false);
        if (!StrUtil.isEmpty(qry.getOrderBy())) {
            page.setOrderBy(qry.getOrderBy().concat(" ").concat(qry.getOrderDirection()));
        }

        // 先查id
        final List<KeyValueItemEntity> itemEntities = keyValueItemService.lambdaQuery()
                .select(KeyValueItemEntity::getId)
                .eq(qry.getItemName() != null, KeyValueItemEntity::getItemName, qry.getItemName())
                .eq(qry.getItemKey() != null, KeyValueItemEntity::getItemKey, qry.getItemKey())
                .eq(KeyValueItemEntity::getDeleted, 0)
                .list();
        final List<Integer> idList = itemEntities
                .stream().map(KeyValueItemEntity::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(idList)) {
            return new PageInfo<>();
        }
        // 再根据id查数据
        final List<KeyValueItemCo> list = keyValueItemService.lambdaQuery()
                .in(KeyValueItemEntity::getId, idList)
                .list()
                // 转化
                .stream()
                .map(keyValueItemEntity -> ConvertUtils.convert(keyValueItemEntity, KeyValueItemCo.class))
                .collect(Collectors.toList());
        return PageInfoHelper.getPageInfo(itemEntities, list);

    }
}
