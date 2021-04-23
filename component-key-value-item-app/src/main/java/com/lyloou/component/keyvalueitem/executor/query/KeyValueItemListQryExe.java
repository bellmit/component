package com.lyloou.component.keyvalueitem.executor.query;

import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemListQry;
import com.lyloou.component.keyvalueitem.repository.entity.KeyValueItemEntity;
import com.lyloou.component.keyvalueitem.repository.service.KeyValueItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lilou
 * @since 2021/4/16
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyValueItemListQryExe {
    private final KeyValueItemService keyValueItemService;

    public List<KeyValueItemCo> execute(KeyValueItemListQry qry) {
        return keyValueItemService.lambdaQuery()
                .eq(KeyValueItemEntity::getItemName, qry.getItemName())
                .eq(qry.getItemKey() != null, KeyValueItemEntity::getItemKey, qry.getItemKey())
                .eq(KeyValueItemEntity::getDeleted, 0)
                .list()
                .stream()
                .map(keyValueItemEntity -> {
                    final KeyValueItemCo keyValueItemCo = new KeyValueItemCo();
                    BeanUtils.copyProperties(keyValueItemEntity, keyValueItemCo);
                    return keyValueItemCo;
                })
                .collect(Collectors.toList());
    }
}
