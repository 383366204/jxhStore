package com.yjcloud.asr.video.utils.jsonResult;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @Author qyw
 * @Description 服务响应通用对象
 * @Date Created in 20:51 2019/5/27
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class  ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = -206696267977993096L;
    
    private  int code;
    private  String msg;
    private  T data;

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
        return  this.code == 1;
    }

    public  static <T> ServerResponse<T> success(){
        return  new ServerResponse<>(ServerCodeEnum.SUCCESS.getCode());
    }

    public  static <T> ServerResponse<T> success(String msg){
        return  new ServerResponse<>(ServerCodeEnum.SUCCESS.getCode(),msg);
    }

    public  static <T> ServerResponse<T> success(T data){
        return  new ServerResponse<>(ServerCodeEnum.SUCCESS.getCode(),data);
    }


    public  static <T> ServerResponse<T> success(String msg,T data){
        return  new ServerResponse<>(ServerCodeEnum.SUCCESS.getCode(),msg,data);
    }


    public  static <T> ServerResponse<T> error(String msg,T data){
        return  new ServerResponse<>(ServerCodeEnum.ERROR.getCode(),msg,data);
    }

    public  static <T> ServerResponse<T> error(){
        return  new ServerResponse<>(ServerCodeEnum.ERROR.getCode(),ServerCodeEnum.ERROR.getDesc());
    }

    public  static <T> ServerResponse<T> error(String errorMsg){
        return  new ServerResponse<>(ServerCodeEnum.ERROR.getCode(),errorMsg);
    }

    public  static <T> ServerResponse<T> error(int code,String errorMsg){
        return  new ServerResponse<>(code,errorMsg);
    }

    public  static <T> ServerResponse<T> error(int code,String errorMsg,T date){
        return  new ServerResponse<>(code,errorMsg,date);
    }
}
