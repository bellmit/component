package com.lyloou.component.dto.codemessage;

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
public enum CommonCodeMessage implements CodeMessage {
    SUCCESS("100000", "成功"),
    ERROR("100002", "系统繁忙"),

    BIZ_ERROR("100003", "业务异常"),

    // input
    ILLEGAL_REQUEST("100013", "无效请求"),
    ILLEGAL_PARAM("100014", "参数错误"),
    ILLEGAL_DATA("100015", "非法数据"),
    ILLEGAL_IP("100016", "ip来源不合法"),
    NOT_FOUND("100017", "路径不存在，请检查路径是否正确"),
    REQUEST_REPEATEDLY("100018", "重复请求操作"),
    REQUEST_FREQUENTLY("100019", "请求过于频繁"),


    // compute
    NOT_EXISTED_ENTITY("100021", "实体不存在"),
    FORBIDDEN("100021", "没有权限，请联系管理员授权"),

    // db
    DB_ERROR("100031", "数据库异常"),


    ;

    private final String code;
    private final String message;

    CommonCodeMessage(String code, String message) {
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