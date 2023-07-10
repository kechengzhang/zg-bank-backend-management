package com.vip.file.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zw
 * @date 2022/8/12 17:56
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "新增分视频参数")
public class FileLockDto {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "事件id")
    private String targetId;

    @ApiModelProperty(value = "是否正在合成或已经合成成功 0否1是")
    private String isCompose;
}
