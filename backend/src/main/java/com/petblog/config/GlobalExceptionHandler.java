package com.petblog.config;

import com.petblog.dto.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理系统中的异常，返回标准化的错误响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理运行时异常
     * 捕获业务逻辑抛出的异常
     * @param e 运行时异常
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    /**
     * 处理参数校验异常
     * 捕获@Valid注解校验失败的异常
     * @param e 参数校验异常
     * @return 包含校验错误信息的响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException e) {
        // 获取第一个校验错误信息
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error(message);
    }

    /**
     * 处理其他未知异常
     * 捕获所有未被处理的异常
     * @param e 异常
     * @return 通用错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.error("服务器内部错误");
    }
}
