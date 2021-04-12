package com.lyloou.component.schedulemonitor;

import com.lyloou.component.dto.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ScheduleMonitorController {

    @Autowired
    ScheduleMonitorHandler handler;

    @RequestMapping("/list")
    public SingleResponse<Map<String, Boolean>> list() {
        return SingleResponse.buildSuccess(handler.listKeyStatus());
    }


    @RequestMapping("/get")
    public SingleResponse<Boolean> get(String key) {
        if (!handler.isKeyExisted(key)) {
            return SingleResponse.buildFailure("key is invalid");
        }

        final Boolean data = handler.getStatus(key);
        return SingleResponse.buildSuccess(data);
    }

    @RequestMapping("/put")
    public SingleResponse<String> put(String key, Boolean status) {
        if (status == null || !handler.isKeyExisted(key)) {
            return SingleResponse.buildFailure("key or value is invalid");
        }

        handler.putKeyStatus(key, status);
        final String result = String.format("key为%s的定时器%s", key, getStatusMsg(status));
        return SingleResponse.buildSuccess(result);
    }

    @RequestMapping("/putAllStatus")
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

    @RequestMapping("/call")
    public SingleResponse<String> call(String key) {
        if (!handler.isKeyExisted(key)) {
            return SingleResponse.buildFailure("key is invalid");
        }

        final boolean result = handler.call(key);
        return SingleResponse.buildSuccess(String.format("调用%s的状态为：%s", key, result));
    }
}
