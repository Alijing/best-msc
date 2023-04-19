package com.jing.common.core.exception;

import com.jing.common.core.base.BaseResp;
import com.jing.common.core.enums.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.core.exception
 * @date : 2022/5/31 16:09
 * @description :
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * -------- 通用异常处理方法 --------
     */
    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public BaseResp<Object> error(Exception e) {
        logger.error(e.getMessage(), e);
        // 通用异常结果
        String description = e.getCause().getMessage();
        return BaseResp.error(description);
    }

    /**
     * -------- 指定异常处理方法 --------
     */
    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public BaseResp<Object> error(NullPointerException e) {
        logger.error(e.getMessage(), e);
        return BaseResp.setResult(ResultEnum.NULL_POINT);
    }

    @ResponseBody
    @ExceptionHandler(HttpClientErrorException.class)
    public BaseResp<Object> error(HttpClientErrorException e) {
        logger.error(e.getMessage(), e);
        return BaseResp.setResult(ResultEnum.HTTP_CLIENT_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResp<Object> error(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return BaseResp.setResult(ResultEnum.METHOD_NOT_ALLOWED);
    }

    /**
     * -------- 自定义定异常处理方法 --------
     */
    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public BaseResp<Object> error(CustomException e) {
        logger.error(e.getMessage(), e);
        return BaseResp.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResp<Object> error(MethodArgumentNotValidException e) {
        logger.error(e.getMessage(), e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String description = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).reduce((x, y) -> x + "," + y).orElseGet(ResultEnum.PARAM_ERROR::getMessage);
        return BaseResp.error(ResultEnum.PARAM_ERROR.getCode(), description);
    }

    /**
     * sql执行异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public BaseResp<Object> error(SQLIntegrityConstraintViolationException e) {
        logger.error(e.getMessage(), e);
        return BaseResp.setResult(ResultEnum.SQL_EXCEPTION);
    }

    /**
     * 关键词重复
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(DuplicateKeyException.class)
    public BaseResp<Object> error(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return BaseResp.setResult(ResultEnum.DUPLICATE_KEY);
    }

}
