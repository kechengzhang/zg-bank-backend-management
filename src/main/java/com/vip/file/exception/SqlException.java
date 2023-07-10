package com.vip.file.exception;

/**
 * @author zkc
 * @date 2021/06/01
 * sql错误异常
 */
public class SqlException extends  RuntimeException{
    private int code;
    private String msg;
    public SqlException(){
    }
    public SqlException(String msg){
        this.msg=msg;
    }

    public SqlException(int code, String msg){
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
