package com.newland.jxh.store.common.jsonResult;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
* @Author qyw
* @Description 服务响应通用对象
* @Date Created in 20:51 2018/8/11
* @Param
* @Return
**/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class  ServerResponse<T> implements Serializable {

    private  int code;
    private  String msg;
    private T data;

    private ServerResponse(int code) {
        this.code = code;
    }
    private ServerResponse(String msg) {
        this.msg = msg;
    }
    private ServerResponse(T data) {
        this.data = data;
    }

    private ServerResponse(int code,String msg) {
        this.code=code;
        this.msg = msg;
    }
    private ServerResponse(int code,T data) {
        this.code=code;
        this.data = data;
    }

    private ServerResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
    @JsonIgnore
    public  boolean isSuccess(){
        return  this.code==1;
    }

    public  static <T> ServerResponse<T> createBySuccess(){
        return  new ServerResponse<T>(ServerCode.SUCCESS.getCode());
    }

    public  static <T> ServerResponse<T> createBySuccess(String msg){
        return  new ServerResponse<T>(ServerCode.SUCCESS.getCode(),msg);
    }

    public  static <T> ServerResponse<T> createBySuccess(T data){
        return  new ServerResponse<T>(ServerCode.SUCCESS.getCode(),data);
    }

    public  static <T> ServerResponse<T> createByError(String msg,T data){
        return  new ServerResponse<T>(ServerCode.ERROR.getCode(),msg,data);
    }


    public  static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return  new ServerResponse<T>(ServerCode.SUCCESS.getCode(),msg,data);
    }


    public  static <T> ServerResponse<T> createByError(){
        return  new ServerResponse<T>(ServerCode.ERROR.getCode(),ServerCode.ERROR.getDesc());
    }

    public  static <T> ServerResponse<T> createByError(String errorMsg){
        return  new ServerResponse<T>(ServerCode.ERROR.getCode(),errorMsg);
    }

    public  static <T> ServerResponse<T> createByError(int code,String errorMsg){
        return  new ServerResponse<T>(code,errorMsg);
    }

    public  static <T> ServerResponse<T> createByError(int code,String errorMsg,T date){
        return  new ServerResponse<T>(code,errorMsg,date);
    }
 }
