package com.lyloou.component.keyvalueitem.service;


import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemForAppCo;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemDeleteCmd;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemSaveOrUpdateCmd;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemGetQry;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemListQry;

import java.util.List;

/**
 * @author lilou
 * @since 2021/3/19
 */
public interface KeyValueItemAdminService {
    /**
     * 列出键值项
     *
     * @param qry 查询参数
     * @return 键值项列表
     */
    List<KeyValueItemCo> listKeyValueItem(KeyValueItemListQry qry);


    /**
     * 保存或更新键值项
     *
     * @param cmd 更新参数
     * @return 操作结果
     */
    boolean saveOrUpdateKeyValueItem(KeyValueItemSaveOrUpdateCmd cmd);


    /**
     * 删除键值项
     *
     * @param cmd 删除参数
     * @return 结果
     */
    boolean deleteKeyValueItem(KeyValueItemDeleteCmd cmd);

    /**
     * 获取项
     *
     * @param qry 查询参数
     * @return 结果
     */
    KeyValueItemForAppCo getKeyValueItem(KeyValueItemGetQry qry);
}
