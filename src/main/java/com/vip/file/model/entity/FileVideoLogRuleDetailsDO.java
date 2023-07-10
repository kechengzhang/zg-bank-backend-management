package com.vip.file.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/31 13:33 星期三
 * @Version 1.0
 */
@Data
public class FileVideoLogRuleDetailsDO {
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;
    @ApiModelProperty(value = "操作类型")
    private String operationType;
    @ApiModelProperty(value = "失败原因")
    private String failureReason;
    @ApiModelProperty(value = "输入内容")
    private String inputContent ;
    @ApiModelProperty(value = "请求参数")
    private String requestParameter;
    @ApiModelProperty(value = "操作对象")
    private String operationObjects;
    @ApiModelProperty(value = "结果")
    private String operationResult;

}
