package com.lyloou.component.keyvallueitem.controller;

import com.lyloou.component.keyvallueitem.AbstractTest;
import com.lyloou.component.keyvalueitem.dto.clientobject.KeyValueItemCo;
import com.lyloou.component.keyvalueitem.dto.command.KeyValueItemSaveOrUpdateCmd;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author lilou
 * @since 2021/4/19
 */
public class KeyValueItemControllerTest extends AbstractTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void saveOrUpdateKeyValueItem() throws Exception {
        System.out.println("1");
        final KeyValueItemSaveOrUpdateCmd cmd = new KeyValueItemSaveOrUpdateCmd();
        final KeyValueItemCo keyValueItem = new KeyValueItemCo();
        keyValueItem.setItemName("itemName");
        keyValueItem.setItemKey("itemKey");
        keyValueItem.setItemValue("itemValue");
        cmd.setKeyValueItem(keyValueItem);

        final String queryData = super.mapToJson(cmd);
        String uri = "/key-value-item/saveOrUpdate";
        final MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(queryData)
        ).andReturn();
        final int status = result.getResponse().getStatus();
        Assert.assertEquals(200, status);
    }

    @Test
    public void listKeyValueItem() throws Exception {
        System.out.println("2");
        String uri = "/key-value-item/list";
        final MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                .queryParam("itemName", "itemName")
                .queryParam("itemKey", "itemKey")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        final String contentAsString = response.getContentAsString();
        System.out.println(contentAsString);
        final int status = response.getStatus();
        Assert.assertEquals(200, status);
    }

    @Test
    public void deleteKeyValueItem() throws Exception {
        System.out.println("2");
        String uri = "/key-value-item/delete";
        final MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri)
                .queryParam("itemName", "itemName")
                .queryParam("itemKey", "itemKey")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        final MockHttpServletResponse response = result.getResponse();
        final String contentAsString = response.getContentAsString();
        System.out.println(contentAsString);
        final int status = response.getStatus();
        Assert.assertEquals(200, status);
    }
}