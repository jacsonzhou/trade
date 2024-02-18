package com.ybb.trade.common;

import lombok.Data;

@Data
public class MessageResult {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private Object data;
    public MessageResult() {

    }
    public MessageResult(int code, String msg, Object object) {
        this.code = code;
        this.message = msg;
        this.data = object;
    }
    public static MessageResult success() {
        return getInstance(0, "SUCCESS", null);
    }

    public static MessageResult success(String msg) {
        return getInstance(0, msg,null);
    }

    public static MessageResult success(Object data) {
        return getInstance(0, "SUCCESS",data);
    }

    public static MessageResult success(String msg, Object data) {
        return getInstance(0, msg, data);
    }

    public static MessageResult error(int code, String msg) {
        return getInstance(code, msg, null);
    }

    public static MessageResult error(String msg) {
        return getInstance(500, msg, null);
    }


    private static MessageResult getInstance(int code, String message, Object data) {
        return new MessageResult(code, message, data);
    }
}
