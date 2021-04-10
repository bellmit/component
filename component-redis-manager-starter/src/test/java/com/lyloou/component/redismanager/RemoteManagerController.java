package com.lyloou.component.redismanager;

import com.lyloou.component.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author lilou
 * @since 2021/4/10
 */
@RestController
@RequestMapping("/remote")
public class RemoteManagerController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/deleteUser")
    public Object deleteUser(String key) {
        final String url = String.format("http://localhost:8080/redismanager/del?key=%s&prefix=%s", key, CacheNames.TEST_USER);
        return restTemplate.getForEntity(url, Result.class);
    }
}
