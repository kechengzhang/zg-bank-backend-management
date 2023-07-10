package com.vip.file.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/30 15:39 星期二
 * @Version 1.0
 */
@Data
@ApiModel("规则详情")
public class FileVideoLogRuleDetailsVO {
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;
    @ApiModelProperty(value = "操作类型")
    private String operationType;
    private List<String> afterModificationFile;
    @ApiModelProperty(value = "失败原因")
    private String failureReason;
    @ApiModelProperty(value = "输入内容")
    private String inputContent ;
    @ApiModelProperty(value = "请求参数")
    private String requestParameter;
    @ApiModelProperty(value = "操作对象")
    private String operationObjects;
    @ApiModelProperty(value = "修改前文件规则名称")
    private List<String> beforeModificationRuleFileName;
    @ApiModelProperty(value = "修改后文件规则名称")
    private List<String> afterModificationRuleFileName;
    @ApiModelProperty(value = "修改前状态")
    private String beforeStatus;
    @ApiModelProperty(value = "修改前状态")
    private String afterStatus;



}
