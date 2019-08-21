package com.websocket.demo.util;

import lombok.Data;

/**
 * @ClassName ApiReturnUtil
 * @Description TODO
 * @Author G-B-X
 * @Date 2019/7/14 12:18
 * @Version 1.0
 **/
@Data
public class ApiReturnUtil<T> {
    // 返回状态码
    private int status;
    // 错误信息
    private String error;
    // 返回数据
    private T data;
    // 返回标志，成功true，错误false
    private boolean renturnCode;

    public static <T> ApiReturnUtil success(T t) {
        return success(t,"成功");
    }

    public static <T> ApiReturnUtil success(T t,String message) {
        ApiReturnUtil returnUtil = new ApiReturnUtil();
        returnUtil.setData(t);
        returnUtil.setStatus(200);
        returnUtil.setError("");
        returnUtil.setRenturnCode(true);
        return returnUtil;
    }

    public static <T> ApiReturnUtil error(T t) {
        return success(t,"失败");
    }

    public static <T> ApiReturnUtil error(T t,String message) {
        ApiReturnUtil returnUtil = new ApiReturnUtil();
        returnUtil.setData(t);
        returnUtil.setStatus(9999);
        returnUtil.setError(message);
        returnUtil.setRenturnCode(false);
        return returnUtil;
    }

}
