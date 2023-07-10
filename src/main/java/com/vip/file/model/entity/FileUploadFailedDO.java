package com.vip.file.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/6/2 13:55 星期五
 * @Version 1.0
 */
@Data
@ApiModel(value = "失败视频上传")
public class FileUploadFailedDO {
    private String bankName;
    private String fileName;
    private String uploadTime;
}
