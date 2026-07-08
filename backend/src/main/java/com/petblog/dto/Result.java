package com.petblog.dto;

import lombok.Data;

/**
 * 统一API响应结果封装
 * 所有Controller接口都使用此类返回结果，保证前端接收数据格式一致
 *
 * @param <T> 响应数据的类型
 */
@Data
public class Result<T> {

    /** 状态码：200-成功，其他-失败 */
    private int code;

    /** 提示信息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 成功返回（带数据） */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /** 成功返回（带消息和数据） */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /** 失败返回（仅消息） */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    /** 失败返回（自定义状态码和消息） */
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
