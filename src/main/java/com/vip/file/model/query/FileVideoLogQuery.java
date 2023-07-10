package com.vip.file.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/30 9:14 星期二
 * @Version 1.0
 */
@Data
@ApiModel(value = "用户操作记录查询参数")
public class FileVideoLogQuery {
    @ApiModelProperty(value = "当前页数")
    private  Integer pageNumber;
    @ApiModelProperty(value = "每页条数")
    private  Integer pageSize;
    @ApiModelProperty(value = "系统名称")
    private String systemName;
    @ApiModelProperty(value = "业务模块")
    private String businessModule;
    @ApiModelProperty(value = "操作对象")
    private String operationObjects;
    @ApiModelProperty(value = "操作状态")
    private String operationResult;
    @ApiModelProperty(value = "操作类型")
    private String operationType;
    @ApiModelProperty(value = "开始时间")
    private String beginTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
