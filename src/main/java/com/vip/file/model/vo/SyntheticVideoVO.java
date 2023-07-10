package com.vip.file.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 9:45 星期五
 * @Version 1.0
 */
@Data
public class SyntheticVideoVO {
    private String id;
    @ApiModelProperty(value = "文件名")
    private String fileName;
    @ApiModelProperty(value = "合成时间")
    private String syntheticTime;
    @ApiModelProperty(value = "银行名称")
    private String bankName;
    @ApiModelProperty(value = "文件名全称")
    private String fullName;
}
