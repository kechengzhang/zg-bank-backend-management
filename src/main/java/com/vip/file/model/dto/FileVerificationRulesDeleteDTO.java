package com.vip.file.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.ValidationEvent;
import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/31 10:43 星期三
 * @Version 1.0
 */
@Data
public class FileVerificationRulesDeleteDTO {
    private List<Long> id;
    private List<String> name;
//    @ApiModelProperty(value = "删除类型 1删除，2批量删除")
    private Integer deleteType;
}
