package com.lyloou.component.schedulemonitor;

import com.lyloou.component.dto.SingleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>监控管理api</p>
 *
 * @author lilou
 * @since 2021/3/7
 */
@RestController
@RequestMapping("/schedule/monitor")
@Api(tags = "【component】监控-Scheduler管理接口")
public class ScheduleMonitorController {

    @Autowired
    ScheduleMonitorHandler handler;

    @GetMapping("/list")
    @ApiOperation("列出受控的Schedule状态，以键值对表示")
    public SingleResponse<Map<String, Boolean>> list() {
        return SingleResponse.buildSuccess(handler.listKeyStatus());
    }


    @GetMapping("/get")
    @ApiOperation("获取具体的一个Schedule状态")
    public SingleResponse<Boolean> get(String key) {
        if (!handler.isKeyExisted(key)) {
            return SingleResponse.buildFailure("key is invalid");
        }

        final Boolean data = handler.getStatus(key);
        return SingleResponse.buildSuccess(data);
    }

    @GetMapping("/put")
    @ApiOperation("根据key修改Schedule状态，true：启用；false：禁用")
    public SingleResponse<String> put(String key, Boolean status) {
        if (status == null || !handler.isKeyExisted(key)) {
            return SingleResponse.buildFailure("key or value is invalid");
        }

        handler.putKeyStatus(key, status);
        final String result = String.format("key为%s的定时器%s", key, getStatusMsg(status));
        return SingleResponse.buildSuccess(result);
    }

    @GetMapping("/putAllStatus")
    @ApiOperation("同时修改所有的Schedule状态，true：启用；false：禁用")
    public SingleResponse<String> putAll(Boolean status) {
        if (status == null) {
            return SingleResponse.buildFailure("status is invalid");
        }

        handler.putAllStatus(status);
        return SingleResponse.buildSuccess(String.format("所有的定时器%s", getStatusMsg(status)));
    }

    private String getStatusMsg(Boolean status) {
        return status ? "已启用" : "已禁用";
    }

    @GetMapping("/call")
    @ApiOperation("根据 key 手动调用一个 Schedule")
    public SingleResponse<String> call(String key) {
        if (!handler.isKeyExisted(key)) {
            return SingleResponse.buildFailure("key is invalid");
        }

        final boolean result = handler.call(key);
        return SingleResponse.buildSuccess(String.format("调用%s的状态为：%s", key, result));
    }
}
