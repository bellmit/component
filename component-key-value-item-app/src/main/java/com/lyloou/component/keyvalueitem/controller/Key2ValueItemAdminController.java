package com.lyloou.component.keyvalueitem.controller;


import com.lyloou.component.dto.PageResponse;
import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemForAppCo;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemDeleteCmd;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemSaveOrUpdateCmd;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemGetQry;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemListQry;
import com.lyloou.component.keyvalueitem.dto.command.query.KeyValueItemPageQry;
import com.lyloou.component.keyvalueitem.service.KeyValueItemAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 通用 前端控制器
 * </p>
 *
 * @author lilou
 * @since 2021-03-17
 */
@RestController
@Api(tags = "[通用2]-键值项接口")
@RequestMapping("/api/key-value-item")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class Key2ValueItemAdminController {
    private final KeyValueItemAdminService keyValueItemAdminService;

    @ApiOperation(value = "获取键值项列表", notes = "根据参数获取键值项列表")
    @GetMapping("list")
    public SingleResponse<List<KeyValueItemCo>> listKeyValueItem(@Valid KeyValueItemListQry qry) {
        return SingleResponse.buildSuccess(keyValueItemAdminService.listKeyValueItem(qry));
    }

    @ApiOperation(value = "获取键值项", notes = "根据参数获取具体键值项")
    @GetMapping("get")
    public SingleResponse<KeyValueItemForAppCo> getKeyValueItem(@Valid KeyValueItemGetQry qry) {
        return SingleResponse.buildSuccess(keyValueItemAdminService.getKeyValueItem(qry));
    }

    @ApiOperation(value = "分页获取键值项", notes = "根据参数获取键值项")
    @GetMapping("page")
    public PageResponse<KeyValueItemCo> pageKeyValueItem(@Valid KeyValueItemPageQry qry) {
        return keyValueItemAdminService.pageKeyValueItem(qry);
    }

    @ApiOperation(value = "删除键值项", notes = "根据参数删除指定的键值项")
    @PostMapping("delete")
    public SingleResponse<Boolean> deleteKeyValueItem(@Valid KeyValueItemDeleteCmd cmd) {
        return SingleResponse.buildSuccess(keyValueItemAdminService.deleteKeyValueItem(cmd));
    }

    @ApiOperation(value = "保存或更新键值项", notes = "根据传入的参数信息保存或更新参数")
    @PostMapping("saveOrUpdate")
    public SingleResponse<Boolean> saveOrUpdateKeyValueItem(@Valid @RequestBody KeyValueItemSaveOrUpdateCmd cmd) {
        return SingleResponse.buildSuccess(keyValueItemAdminService.saveOrUpdateKeyValueItem(cmd));
    }
}
