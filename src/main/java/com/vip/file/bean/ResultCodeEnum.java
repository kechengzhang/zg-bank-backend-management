package com.vip.file.bean;

/**
 * @author zkc
 *
 */
public enum ResultCodeEnum {
    /**
     *
     *
     */
    SUCCESS("00000", "success"),
    USERNAME_ERROR("A0001", "用户名错误"),
    PASSWORD_ERROR("A0002", "密码错误"),
    PARAMETER_ERROR("A0003", "参数有误"),
    TOKEN_ERROR("A0004", "token无效"),
    OLD_PASSWORD_ERROR("A0005", "原始密码错误"),
    USERNAME_NULL_ERROR("A0022", "用户名不能为空"),
    PASSWORD_NULL_ERROR("A0023", "密码不能为空"),
    COMPOSE_ERROR("A0006", "合成时间已到，禁止上传"),
    FILE_ALREADY_EXIST_ERROR("A0007", "文件已存在"),
    VIDEO_DELETE_ERROR("A0008", "总行无法删除/修改视频信息"),
    DELETE_ERROR("A0009", "删除失败"),
    VERIFICATION_NAME_ERROR("A0010", "规则名已存在，请重新输入"),
    ERROR("B0001", "未知异常"),
    SQL_EXCEPTION("B0002", "SQL异常"),
    UPLOAD_ERROR("B0003", "文件上传失败"),
    SAVE_SUCCESS("00000", "新增成功"),
    DELETE_SUCCESS("00000", "删除成功"),
    UPDATE_SUCCESS("00000", "修改成功"),
    QUERY_SUCCESS("00000", "查询成功"),
    QUERY_VIDEO_COMPOSE_SUCCESS("00000", "暂无合成视频"),
    USER_LOGIN_OUT_SUCCESS("00000", "退出登录成功"),
    USER_LOGIN_SUCCESS("00000", "登录成功");

    private String msg;
    private String code;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.msg = message;
    }

    public String getMessage() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    public static String getValue(String index) {
        ResultCodeEnum[] values = values();
        for (ResultCodeEnum value : values) {
            if (value.getCode().equals(index)) {
                return value.msg;
            }
        }
        return null;
    }
}
