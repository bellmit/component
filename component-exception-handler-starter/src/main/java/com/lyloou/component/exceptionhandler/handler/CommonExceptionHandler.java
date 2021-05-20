package com.lyloou.component.exceptionhandler.handler;


import com.lyloou.component.dto.SingleResponse;
import com.lyloou.component.dto.codemessage.CommonCodeMessage;
import com.lyloou.component.exceptionhandler.exception.BizException;
import com.lyloou.component.exceptionhandler.exception.ParamException;
import com.lyloou.component.exceptionhandler.model.ErrorLevel;
import com.lyloou.component.exceptionhandler.service.ExceptionHandlerService;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public SingleResponse<Void> handleAuthorizationException(Exception e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(CommonCodeMessage.FORBIDDEN.appendMessage(e.getMessage()));
    }

    @ExceptionHandler(ParamException.class)
    public SingleResponse<Void> paramException(Exception e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(CommonCodeMessage.ILLEGAL_PARAM.appendMessage(e.getMessage()));
    }

    private void handleThrowable(Throwable e, ErrorLevel level) {
        for (ExceptionHandlerService handlerService : handlerServiceList) {
            handlerService.handle(e, level);
        }
    }

    @ExceptionHandler(Exception.class)
    public SingleResponse<Void> handleException(Exception e) {
        handleThrowable(e, ErrorLevel.ERROR);
        return SingleResponse.buildFailure(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BizException.class)
    public SingleResponse<Void> handleBusinessException(Exception e) {
        handleThrowable(e, ErrorLevel.ERROR);
        return SingleResponse.buildFailure(CommonCodeMessage.BIZ_ERROR.appendMessage(e.getMessage()));
    }

    /**
     * 可能需要添加以下配置，才能监听得到
     * spring.mvc.throw-exception-if-no-handler-found=true
     * # 不要为我们工程中的资源文件建立映射
     * spring.resources.add-mappings=false
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public SingleResponse<Void> handleNoHandlerFoundException(Exception e) {
        handleThrowable(e, ErrorLevel.WARN);
        return SingleResponse.buildFailure(CommonCodeMessage.NOT_FOUND.appendMessage(e.getMessage()));
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        handleThrowable(e, ErrorLevel.WARN);
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError == null ? "参数无效" : fieldError.getDefaultMessage();
        return SingleResponse.buildFailure(CommonCodeMessage.ILLEGAL_PARAM.appendMessage(message));
    }
}
