package com.rainbow.base.model.base;

import com.rainbow.base.constant.HttpCode;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * 响应信息主体
 *
 * @author rainvom
 */

public class Result<T> implements Serializable {
    // 成功
    public static final int SUCCESS = HttpCode.OK.value();

    //失败
    public static final int ERROR = HttpCode.INTERNAL_SERVER_ERROR.value();

    private int code;
    private String msg;
    private T data;

    // 默认空值
    private static final Map<String, Object> EMPTY_DATA = Collections.emptyMap();

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data != null ? data : (T) EMPTY_DATA;
    }

    public static <T> Result<T> success() {
        return new Result<>(SUCCESS, "操作成功", (T) EMPTY_DATA);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS, "操作成功", data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(SUCCESS, msg, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(ERROR, "操作失败", (T) EMPTY_DATA);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(ERROR, msg, (T) EMPTY_DATA);
    }

    public static <T> Result<T> error(T data) {
        return new Result<>(ERROR, "操作失败", data);
    }

    public static <T> Result<T> error(String msg, T data) {
        return new Result<>(ERROR, msg, data);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, (T) EMPTY_DATA);
    }

    public static <T> Result<T> error(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }

    public static <T> Boolean isError(Result<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(Result<T> ret) {
        return Result.SUCCESS == ret.getCode();
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
