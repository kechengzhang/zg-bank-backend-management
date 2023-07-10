package com.vip.file.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 14:17 星期五
 * @Version 1.0
 */
@Data
public class FileVerificationRulesVO {
    private Long  id;
    @ApiModelProperty("规则名称")
    private String verificationName;
    private String generationTime;
    private String status;
    private String firstFile;
    private String lastFile;
    @ApiModelProperty("计算后规则")
    private String verificationRule;
}
