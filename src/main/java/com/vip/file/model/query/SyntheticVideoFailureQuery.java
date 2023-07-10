package com.vip.file.model.query;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/6/2 13:50 星期五
 * @Version 1.0
 */
@Data
public class SyntheticVideoFailureQuery {
    private Integer pageNumber;
    private Integer pageSize;
    private String beginTime;
    private String endTime;
}
