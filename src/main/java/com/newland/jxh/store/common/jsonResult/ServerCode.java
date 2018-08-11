package com.newland.jxh.store.common.jsonResult;

/**
 * Created by qyw on 2017/5/23.
 */
public enum ServerCode {
    SUCCESS(1,"SUCCESS"),
    ERROR(0,"ERROR"),
    NEED_LOGIN(2,"需要登录"),
    ILLEGAL_ARGUMENTS(3,"参素错误"),
    UNKNOW_EXCEPTION(4,"未知异常");

    private final int code;
    private final String desc;

    ServerCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
