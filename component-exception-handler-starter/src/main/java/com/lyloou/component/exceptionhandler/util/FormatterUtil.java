package com.lyloou.component.exceptionhandler.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author lilou
 * @since 2021/7/16
 */
@Slf4j
public class FormatterUtil {

    public static String format(String messagePattern, Object arg) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg}).getMessage();
    }

    public static String format(String messagePattern, Object arg1, Object arg2) {
        return MessageFormatter.arrayFormat(messagePattern, new Object[]{arg1, arg2}).getMessage();
    }

    public static String format(String messagePattern, Object... argArray) {
        return MessageFormatter.arrayFormat(messagePattern, argArray).getMessage();
    }
}
