package com.lyloou.component.keyvalueitem.service.impl;

import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemForAppCo;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemDeleteCmd;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemSaveOrUpdateCmd;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemGetQry;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemListQry;
import com.lyloou.component.keyvalueitem.executor.KeyValueItemDeleteCmdExe;
import com.lyloou.component.keyvalueitem.executor.KeyValueItemSaveOrUpdateCmdExe;
import com.lyloou.component.keyvalueitem.executor.query.KeyValueItemGetQryExe;
import com.lyloou.component.keyvalueitem.executor.query.KeyValueItemListQryExe;
import com.lyloou.component.keyvalueitem.service.KeyValueItemAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lilou
 * @since 2021/4/16
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyValueItemAdminServiceImpl implements KeyValueItemAdminService {
    private final KeyValueItemDeleteCmdExe keyValueItemDeleteCmdExe;
    private final KeyValueItemSaveOrUpdateCmdExe keyValueItemUpdateCmdExe;
    private final KeyValueItemListQryExe keyValueItemListQryExe;
    private final KeyValueItemGetQryExe keyValueItemGetQryExe;

    @Override
    public List<KeyValueItemCo> listKeyValueItem(KeyValueItemListQry qry) {
        return keyValueItemListQryExe.execute(qry);
    }

    @Override
    public boolean saveOrUpdateKeyValueItem(KeyValueItemSaveOrUpdateCmd cmd) {
        return keyValueItemUpdateCmdExe.execute(cmd);
    }

    @Override
    public boolean deleteKeyValueItem(KeyValueItemDeleteCmd cmd) {
        return keyValueItemDeleteCmdExe.execute(cmd);
    }

    @Override
    public KeyValueItemForAppCo getKeyValueItem(KeyValueItemGetQry qry) {
        return keyValueItemGetQryExe.execute(qry);
    }
}
