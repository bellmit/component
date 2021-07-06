package com.lyloou.component.loggerrequestid;

import com.lyloou.component.dto.SingleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lilou
 * @since 2021/7/6
 */
@SpringBootApplication
@RestController
@Slf4j
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class);
    }

    @GetMapping("/ping")
    public String ping() {
        log.info("ping pong");
        return "{pong}";
    }

    @GetMapping("/filter-list")
    public SingleResponse<List<HashMap<String, Object>>> filterList() {
        log.info("filter");
        final List<HashMap<String, Object>> data = new ArrayList<>();
        final HashMap<String, Object> e = new HashMap<>();
        e.put("a", 1);
        e.put("b", "bbb");
        data.add(e);
        return SingleResponse.buildSuccess(data);
    }

    @GetMapping("/filter-object")
    public SingleResponse<HashMap<String, Object>> filterObject() {
        log.info("filter-object");
        final HashMap<String, Object> e = new HashMap<>();
        e.put("a", 1);
        e.put("b", "bbb");
        return SingleResponse.buildSuccess(e);
    }

}
