package com.vip.file.model.vo;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/6/1 22:00 星期四
 * @Version 1.0
 */
@Data
public class FileUploadFailedVO {
    private String bank;
    private String fileName;
    private String uploadTime;
}
