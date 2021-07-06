package com.lyloou.component.loggerrequeststatistic;

import org.slf4j.helpers.MessageFormatter;

/**
 * @author lilou
 * @since 2021/6/21
 */
public class Main {
    public static void main(String[] args) {
        String msg = "hi, this is {}, and age is {}";
        Object[] arguments = new Object[]{"lilou", 28};
        final String message = MessageFormatter.arrayFormat(msg, arguments).getMessage();
        System.out.println(message);
    }
}
