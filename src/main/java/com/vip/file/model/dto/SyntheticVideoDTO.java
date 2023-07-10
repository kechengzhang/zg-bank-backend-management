package com.vip.file.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/31 10:47 星期三
 * @Version 1.0
 */
@Data
public class SyntheticVideoDTO {
    @ApiModelProperty(value = "合成视频id")
    private List<String> id;
    @ApiModelProperty(value = "合成视频名称")
    private List<String> name;
    @ApiModelProperty(value = "删除类型 1删除，2批量删除")
    private Integer deleteType;
}
