package com.vip.file.common;


public enum ResultEnum {

    SUCCESS(200, "Success"),
    ERROR(500, "Error"),
    EXCEPTION(-2, "Exception"),
    UNKNOWN_ERROR(-3, "Unknown Error"),
    SYSTEM_ERROR(-4, "System Error"),
    PARAM_ERROR(-5, "Param Error"),
    ERROR_RESULT_SIZE(-100, "Result Size Error"),
    SQL_QUERY_ERROR(-101, "Sql Query Error"),
    SQL_EXEC_ERROR(-102, "Sql Execute Error"),
    JWT_LOGIN_ERROR(-201, "LoginName Or PassWrod Error"),
    JWT_TIME_ERROR(-202, "Time Out"),
	HTTP_ERROR(-7,"HttpClient Error");

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResultEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

}
