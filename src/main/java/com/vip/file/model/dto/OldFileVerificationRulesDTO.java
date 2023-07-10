package com.vip.file.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/29 16:57 星期一
 * @Version 1.0
 */
@Data
public class OldFileVerificationRulesDTO {
    private Long id;
    @ApiModelProperty("规则名称")
    private String verificationName;
    private String firstFile;
    private String lastFile;
    private Integer use;
    @ApiModelProperty(value = "状态")
    private String status;
}
