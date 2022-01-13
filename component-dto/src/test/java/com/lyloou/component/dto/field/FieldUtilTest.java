package com.lyloou.component.dto.field;

import lombok.Getter;
import lombok.Setter;

/**
 * 属性工具类测试
 *
 * @author lilou
 * @date 2022/1/13 9:14
 */
public class FieldUtilTest {

    @Setter
    @Getter
    static class Article {
        String articleName;
        String articleContent;
    }


    public static void main(String[] args) {
        // test getter
        System.out.println(FieldUtil.noPrefix(Article::getArticleName));
        System.out.println(FieldUtil.underline(Article::getArticleName));
        System.out.println(FieldUtil.underlineUpper(Article::getArticleContent));
        System.out.println(FieldUtil.toSymbolCase(Article::getArticleName, '$'));


        // test setter
        System.out.println(FieldUtil.noPrefix(Article::setArticleName));
        System.out.println(FieldUtil.underline(Article::setArticleName));
        System.out.println(FieldUtil.underlineUpper(Article::setArticleContent));
        System.out.println(FieldUtil.toSymbolCase(Article::setArticleName, '$'));
    }
}