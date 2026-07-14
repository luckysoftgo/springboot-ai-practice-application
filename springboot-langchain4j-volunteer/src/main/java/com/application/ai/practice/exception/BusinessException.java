package com.application.ai.practice.exception;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public Integer getCode() {
        return code;
    }
}