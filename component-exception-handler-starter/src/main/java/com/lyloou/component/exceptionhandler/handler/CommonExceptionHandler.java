package com.lyloou.component.exceptionhandler.handler;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;
import com.lyloou.component.exceptionhandler.exception.AlertException;
import com.lyloou.component.exceptionhandler.exception.BizException;
import com.lyloou.component.exceptionhandler.exception.CommonException;
import com.lyloou.component.exceptionhandler.model.ErrorLevel;
import com.lyloou.component.exceptionhandler.service.ExceptionHandlerService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用异常处理器
 *
 * @author lilou
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    private final List<ExceptionHandlerService> handlerServiceList = new ArrayList<>();

    public List<ExceptionHandlerService> getHandlerServiceList() {
        return handlerServiceList;
    }

    public void addHandlerService(ExceptionHandlerService... services) {
        handlerServiceList.addAll(Arrays.asList(services));
    }

    public void removeHandlerService(ExceptionHandlerService... services) {
        handlerServiceList.removeAll(Arrays.asList(services));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public SingleResponse<Void> handleAccessDeniedException(Exception e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(CommonCodeMessage.FORBIDDEN.appendMessage(e.getMessage()));
    }


    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    public SingleResponse<Void> handleBizException(BizException e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(e.getCode(), e.getMessage());
    }

    /**
     * 参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public SingleResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(CommonCodeMessage.ILLEGAL_PARAM.code(), e.getMessage());
    }

    /**
     * 非法状态异常
     */
    @ExceptionHandler(IllegalStateException.class)
    public SingleResponse<Void> handleIllegalStateException(IllegalStateException e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(CommonCodeMessage.ILLEGAL_STATUS.code(), e.getMessage());
    }

    /**
     * 提示性异常
     */
    @ExceptionHandler(AlertException.class)
    public SingleResponse<Void> handleAlertException(AlertException e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(e.getCode(), e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(CommonException.class)
    public SingleResponse<Void> handleBusinessException(CommonException e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(e.getCode(), e.getMessage());
    }


    private void handleThrowable(Throwable e, ErrorLevel level) {
        for (ExceptionHandlerService handlerService : handlerServiceList) {
            handlerService.handle(e, level);
        }
    }

    /**
     * 其他的默认异常
     */
    @ExceptionHandler(Exception.class)
    public SingleResponse<Void> handleException(Exception e) {
        handleThrowable(e, ErrorLevel.ERROR);
        return SingleResponse.buildFailure(e.getMessage());
    }


    /**
     * NoHandlerFoundException 异常
     * <p>
     * 需要添加以下配置，才能捕捉的到
     * spring.mvc.throw-exception-if-no-handler-found=true
     * # 不要为我们工程中的资源文件建立映射
     * spring.resources.add-mappings=false
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public SingleResponse<Void> handleNoHandlerFoundException(Exception e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(CommonCodeMessage.NOT_FOUND.appendMessage(e.getMessage()));
    }

    /**
     * 参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SingleResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        handleThrowable(e, ErrorLevel.WARN);
        final String msg = getDefaultMessageStr(e);
        return SingleResponse.buildFailure(CommonCodeMessage.ILLEGAL_PARAM.replaceMessage(msg));
    }


    private String getDefaultMessageStr(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        String defaultMessageStr = CommonCodeMessage.ILLEGAL_PARAM.message();
        if (!CollectionUtil.isEmpty(fieldErrors)) {
            final List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            defaultMessageStr = StrUtil.join(", ", msgList);
        }
        return defaultMessageStr;
    }
}
