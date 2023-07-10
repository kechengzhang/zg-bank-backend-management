package com.vip.file.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/16 11:13 星期五
 * @Version 1.0
 */
@Data
public class FileUploadFailedLogDTO {
    private String msg;
    private List<String> file;
}
