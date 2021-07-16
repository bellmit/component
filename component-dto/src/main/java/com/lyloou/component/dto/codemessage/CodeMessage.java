package com.lyloou.component.dto.codemessage;

/**
 * @author lilou
 */
@FunctionalInterface
public interface CodeMessage {

    /**
     * 错误code 定义码
     * <br/> 编号规则： 10-00-00
     * 10-**-** 表示系统全局的
     * 20-**-** 表示api模块
     * 30-**-** 表示admin模块
     * 40-**-** 表示第三方模块
     * <p>
     * <br/> 第一部分： 二位，系统模块
     * <br/> 第二部分： 二位，业务模块
     * <br/> 第三部分： 二位，具体错误
     * 如：统一的成功Code 为 101000
     *
     * @return 状态码
     */
    String code();

    /**
     * 返回消息
     *
     * @return 消息
     */
    default String message() {
        return null;
    }

    /**
     * 替换内容
     *
     * @param msg 内容
     * @return 消息
     */
    default String replaceMessage(String msg) {
        return msg;
    }

    /**
     * 追加消息内容
     *
     * @param msg 附加内容
     * @return 消息内容
     */
    default String appendMessage(String msg) {
        if (msg == null || msg.trim().length() == 0) {
            return message();
        }
        return message().concat(", ").concat(msg);
    }

}
