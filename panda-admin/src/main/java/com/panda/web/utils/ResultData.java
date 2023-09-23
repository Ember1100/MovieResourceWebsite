package com.panda.web.utils;

import lombok.Data;

@Data
public class ResultData<T> {

    int code;
    String msg;
    T data;

    public ResultData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static <T> ResultData<T> ok(T data) {
        ResultData resultData = new ResultData(200, "操作成功", data);
        return resultData;
    }

    public static ResultData ok() {
        ResultData resultData = new ResultData(200,"操作成功");
        return resultData;
    }


}
