package com.vip.file.exception;

/**
 * @author zkc
 * 自定义异常
 *
 */
public class CustomizeException extends  RuntimeException{
    private int code;
    private String msg;
    public CustomizeException(){
    }
    public CustomizeException(String msg){
        this.msg=msg;
    }

    public CustomizeException(int code, String msg){
        this.code=code;
        this.msg=msg;
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
}
