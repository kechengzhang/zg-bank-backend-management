package com.vip.file.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/6/2 10:07 星期五
 * @Version 1.0
 */
@Data
@ApiModel("合成失败视频")
public class SyntheticVideoFailureVO {
    @ApiModelProperty("文件名")
    private String filename;
    @ApiModelProperty("合成时间")
    private String synthesisTime;
    @ApiModelProperty("银行名称")
    private String bankName;
}
