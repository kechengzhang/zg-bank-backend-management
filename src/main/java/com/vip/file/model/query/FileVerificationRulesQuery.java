package com.vip.file.model.query;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 13:55 星期五
 * @Version 1.0
 */
@Data
public class FileVerificationRulesQuery {
    private Integer pageNumber;
    private Integer pageSize;
    private String verificationName;
    private String beginTime;
    private String endTime;
    private Integer use;
}
