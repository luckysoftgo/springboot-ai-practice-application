package com.application.ai.practice.model.basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局统一返回体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResult<T> {

    private Integer code;
    private String msg;
    private T data;

    public static <T> BaseResult<T> success(T data) {
        BaseResult<T> result = new BaseResult<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> BaseResult<T> success() {
        return success(null);
    }

    public static <T> BaseResult<T> fail(Integer code, String msg) {
        BaseResult<T> result = new BaseResult<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> BaseResult<T> fail(String msg) {
        return fail(500, msg);
    }


}