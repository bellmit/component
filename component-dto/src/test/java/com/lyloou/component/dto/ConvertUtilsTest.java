package com.lyloou.component.dto;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class ConvertUtilsTest extends TestCase {

    public void testConvertStr() {
        Response response = new Response();
        response.setCode("111");
        response.setMessage("msg");
        response.setSuccess(true);
        String s = JSON.toJSONString(response);
        System.out.println(s);

        System.out.println(ConvertUtils.<Response>convertStr(s, Response.class));
        System.out.println(ConvertUtils.<Response>convertStr("2{\"code\":\"111\",\"message\":\"msg\",\"success\":true}", Response.class, true));
        System.out.println(ConvertUtils.<Response>convertStr("2{\"code\":\"111\",\"message\":\"msg\",\"success\":true}", Response.class, false));
    }
}