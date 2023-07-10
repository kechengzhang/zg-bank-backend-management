package com.vip.file.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/25 15:37 星期四
 * @Version 1.0
 */
@Data
public class UserLoginVO {
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "调取接口所用token")
    private String token;
}
