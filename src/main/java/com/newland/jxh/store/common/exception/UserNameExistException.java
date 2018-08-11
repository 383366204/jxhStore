package com.newland.jxh.store.common.exception;

/**
 * @Author qyw
 * @Description 用户名已存在异常
 * @Date Created in 20:52 2018/7/22
 */
public class UserNameExistException extends StoreException {
    public UserNameExistException() {
        super("此用户名已存在!");
    }
    public UserNameExistException(String message) {
        super(message);
    }
}
