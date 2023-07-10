package com.vip.file.model.entity;

import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zw
 * @date 2022/7/27 14:00
 */
@Data
@ApiModel(value = "断点续传参数")
public class UploadFileParams extends UploadFileParam {
    @ApiModelProperty(value = "总视频id", example = "0", required = true)
    private String targetId;
    @ApiModelProperty(value = "文件类型", example = "0", required = true)
    private String type ;
    @ApiModelProperty(value = "文件唯一id，由前端控制", example = "0", required = true)
    private String uid;
    @ApiModelProperty(value = "事件id，目前暂定值和target相同", example = "0", required = true)
    private String eventId;
    @ApiModelProperty(value = "当前事件所有文件名称", example = "0", required = true)
    private String fileNames;
    @ApiModelProperty(value = "剩余文件数", example = "0", required = true)
    private Integer fileLength;
    @ApiModelProperty(value = "当前批次文件总数", example = "0", required = true)
    private Integer fileCount;
    @ApiModelProperty(value = "用于区分用户id", example = "0", required = true)
    private String sessionId;
    @ApiModelProperty(value="银行code")
    private String bankBranchId;
    @ApiModelProperty(value="银行名称")
    private String bankBranchName;
    @ApiModelProperty(value="唯一标识,保存日志使用")
    private String uniqueIdentification;
}
