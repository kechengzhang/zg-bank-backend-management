package com.vip.file.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 9:33 星期五
 * @Version 1.0
 */
@Data
@ApiModel(value = "合成视频查询条件")
public class SyntheticVideoQuery {
    @ApiModelProperty(value = "当前页数")
    private Integer pageNumber;
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
    @ApiModelProperty(value = "银行名称")
    private String bankBranchId;
    @ApiModelProperty(value = "开始时间")
    private String beginTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
