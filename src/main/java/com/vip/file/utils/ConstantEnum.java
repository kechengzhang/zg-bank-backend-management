package com.vip.file.utils;

/**
 * @author zkc
 */
public enum ConstantEnum {
    AUDIO_PATH(1,"/audio/"),
    FORMAT(2,"%s -i %s -c copy -bsf:v h264_mp4toannexb -f mpegts %s"),
    /**
     * 单个视频使用
     */
    FORMAT1 (3,"%s -i %s -c:v libx264 -c:a aac -movflags +faststart %s"),
    IP(4, "0:0:0:0:0:0:0:1"),
    ACCESS_TOKEN(5, "Accesstoken"),
    FILE_VERIFICATION_RULES_SAVE(6, "文件名校验规则自定义新增"),
    FILE_VERIFICATION_RULES_UPDATE(7, "文件名校验规则自定义修改"),
    FILE_VERIFICATION_RULES_DELETE(8, "文件名校验规则自定义删除"),
    FILE_ALL_VERIFICATION_RULES_DELETE(9, "文件名校验规则批量删除"),
    SYNTHETIC_VIDEO_DELETE(10, "合成视频列表删除"),
    SYNTHETIC_ALL_VIDEO_DELETE(11, "合成视频列表批量删除"),
    PASSWORD_UPDATE(12, "修改密码修改"),
    CUSTOM_COMPOSITION_UPDATE(13, "定时合成设置修改"),
    CUSTOM_COMPOSITION_CORE_UPDATE(14, "合成并发数设置修改"),
    USER_LOGIN(15, "用户登录登录"),
//    FILE_UPLOAD(16, "上传文件上传"),
    FILE_UPLOAD(16, "添加分视频文件上传"),
    SPLIT_VIDEO_DELETE(17, "删除分视频文件删除"),
    UNKNOWN(18, "unknown"),
    APP_KEY(19, "videorenzhen"),
    TOKEN_URL(20, "/RZIMS-api/video/v1/getAppToken"),
    APP_SECRET(21, "unknown"),
    SET_VIDEO_FILE_URL(22, "/RZIMS-api/video/v1/setVideoFile"),
    SPLIT_VIDEO_FILE(23, ":8095/videoFile/"),
//    FFMPEG(24, "/home/user/bankbranch/java/ffmpeg"),
//    //文件保存路径
////    SAVE_PATH(25, "/home/user/bankbranch/java/file-manager"),
//    SAVE_PATH(25, "D:\\temp\\file-manager"),
//    //断点续传文件配置路径
////    CONF_PATH(26, "/home/user/bankbranch/java/file-manager/conf"),
//    CONF_PATH(26, "D:\\temp\\file-manager\\conf"),
//    //合成视频存放路径
////    COMPOSE_PATH(27, "/home/user/bankbranch/java/video/"),
//    COMPOSE_PATH(27, "D:\\temp\\video\\"),
    //nginx合成视频映射地址
    REQUEST_PATH(28, "/video/"),
    FILE_NAME_CHECK(29, "文件名校验上传");
    /**
     * code值
     */
    private Integer code;
    /**
     * 值
     */
    private String value;

    ConstantEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
