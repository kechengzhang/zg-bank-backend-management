package com.vip.file.exception;

import lombok.Data;

/**
 * @author zkc
 *
 *
 */
@Data
public class JwtDecodeException extends  RuntimeException{
    private int code;
    private String msg;
    public JwtDecodeException(String msg){
        this.msg=msg;
    }
}
