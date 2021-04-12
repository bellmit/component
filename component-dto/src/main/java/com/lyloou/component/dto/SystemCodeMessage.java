package com.lyloou.component.dto;

/**
 * 错误code 定义码
 * <br/> 编号规则： 10-00-00
 * 10-**-** 表示系统全局的
 * 20-**-** 表示api模块
 * 30-**-** 表示admin模块
 * <p>
 * <br/> 第一部分： 二位，系统模块
 * <br/> 第二部分： 二位，业务模块
 * <br/> 第三部分： 二位，具体错误
 * 如：统一的成功Code 为 101000
 *
 * @author lilou
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum SystemCodeMessage implements CodeMessage {
    SUCCESS("100000", "成功"),
    SYSTEM_ERROR("100002", "系统错误"),

    ILLEGAL_REQUEST("100004", "非法请求"),

    ILLEGAL_DATA("100010", "输入非法数据"),
    ENTITY_NOT_EXISTS("100011", "实体不存在"),

    REPEAT_ERROR("100020", "重复操作"),
    FREQUENT_REQUEST("100021", "请求过于频繁"),
    ;

    private final String code;
    private final String message;

    SystemCodeMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}