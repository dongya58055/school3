package com.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通用响应封装类（支持 Swagger 3 和 SpringDoc）
 */
@Data
@Schema(name = "Result", description = "全局统一返回结果")
public class Result<T> {

    @Schema(description = "返回码", example = "200")
    private Integer code;

    @Schema(description = "返回消息", example = "操作成功")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    // 私有构造函数，强制使用静态方法构建
    private Result() {}

    // 构建通用结果
    private static <T> Result<T> build(T data, Integer code, String message) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<T>();
        if (data != null)
            result.setData(data);
        return result;
    }
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
    /** 成功返回（无数据） */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /** 成功返回（附带数据） */
    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMessage());
    }

    /** 失败返回（无数据） */
    public static <T> Result<T> fail() {
        return fail(null);
    }

    /** 失败返回（附带数据） */
    public static <T> Result<T> fail(T data) {
        return build(data, ResultCodeEnum.FAIL.getCode(), ResultCodeEnum.FAIL.getMessage());
    }

    /** 自定义状态返回 */
    public static <T> Result<T> of(T data, ResultCodeEnum codeEnum) {
        return build(data, codeEnum.getCode(), codeEnum.getMessage());
    }

    /** 链式设置消息 */
    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /** 链式设置返回码 */
    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    /** 判断是否为成功结果 */
    public boolean isOk() {
        return ResultCodeEnum.SUCCESS.getCode().equals(this.code);
    }

	
}
