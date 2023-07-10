package com.vip.file.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zw
 * @date 2022/7/21 16:43
 */
@Data
@ApiModel(value = "编辑总视频参数")
public class UploadFileDto implements Serializable {

    @NotNull(message = "合成视频id不能为空")
    @ApiModelProperty(value = "id")
    private String id;

    @NotNull(message = "发生地不能为空")
    @ApiModelProperty(value = "发生地")
    private String occurPlace;

    @ApiModelProperty(value = "事件唯一标识")
    private String event;


    @NotNull(message = "发生时间不能为空")
    @ApiModelProperty(value = "发生时间")
    private Date occurTime;

    @ApiModelProperty(value = "合并后的地址")
    private Date filePath;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "回调ip")
    private String ip;

    @ApiModelProperty(value = "回调port")
    private String port;
    @ApiModelProperty(value = "银行id")
    private String bankBranchId;
    @ApiModelProperty(value = "银行名称")
    private String bankBranchName;

}
