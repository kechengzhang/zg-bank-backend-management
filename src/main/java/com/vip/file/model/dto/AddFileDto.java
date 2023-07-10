package com.vip.file.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取文件信息DTO
 *
 * @author wgb
 * @date 2020/6/9 10:13
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "新增分视频参数")
public class AddFileDto {

    @ApiModelProperty(value = "文件ID")
    private String id;

    @ApiModelProperty(value = "目标对象ID")
    private String targetId;

    @ApiModelProperty(value = "文件位置")
    private String filePath;

    @ApiModelProperty(value = "原始文件名")
    private String fileName;

    @ApiModelProperty(value = "md5")
    private String md5;

    @ApiModelProperty(value = "chunk")
    private Integer chunk;

    @ApiModelProperty(value = "chunks")
    private Integer chunks;

    @ApiModelProperty(value = "size")
    private Long size;

    @ApiModelProperty(value = "是否已经全部上传 0否1是")
    private Integer status;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value="银行code")
    private String bankBranchId;
    @ApiModelProperty(value="银行名称")
    private String bankBranchName;
}
