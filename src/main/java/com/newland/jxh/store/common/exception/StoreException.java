package com.newland.jxh.store.common.exception;

/**
 * @Author qyw
 * @Description 应用异常基类,自定义运行时异常都应该继承此类
 * @Date Created in 19:58 2018/8/11
 */
public class StoreException extends RuntimeException{

    public StoreException()
    {
        super();
    }

    public StoreException(String message)
    {
        super(message);
    }

    public StoreException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public StoreException(Throwable cause)
    {
        super(cause);
    }
}
