package com.vip.file.model.vo;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/7/3 14:43 星期一
 * @Version 1.0
 */
@Data
public class FileTargetIdSizeVO {
    private String targetId;
    private String filePath;
    private Long  size;
}
