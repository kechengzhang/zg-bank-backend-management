package com.vip.file.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 13:34 星期五
 * @Version 1.0
 */
@Data
@ApiModel("文件名检验规则")
public class NewFileVerificationRulesDTO {
    private Long id;
    @ApiModelProperty ("规则名称")
    private String verificationName;
    private String firstFile;
    private String lastFile;
    private Integer use;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "计算后规则")
    private String verificationRule;
}
