package com.application.ai.practice.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.stream.Collectors;
import com.application.ai.practice.model.basic.BaseResult;
import jakarta.validation.ConstraintViolation;

/**
 * 全局异常拦截器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResult<?> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage(), e);
        return BaseResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * @RequestBody 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult<?> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));
        log.error("参数校验异常：{}", msg);
        return BaseResult.fail(400, msg);
    }

    /**
     * @RequestParam 单个参数校验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult<?> handleConstraintViolationException(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        log.error("参数校验异常：{}", msg);
        return BaseResult.fail(400, msg);
    }

    /**
     * 表单绑定校验异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResult<?> handleBindException(BindException e) {
        String msg = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));
        log.error("表单参数校验异常：{}", msg);
        return BaseResult.fail(400, msg);
    }

    /**
     * 未知系统异常兜底
     */
    @ExceptionHandler(Exception.class)
    public BaseResult<?> handleException(Exception e) {
        log.error("系统未知异常", e);
        return BaseResult.fail(500, "服务器内部错误，请稍后重试");
    }
}