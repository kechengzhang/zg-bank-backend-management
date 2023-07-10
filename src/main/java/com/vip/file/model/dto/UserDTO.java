package com.vip.file.model.dto;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/29 17:07 星期一
 * @Version 1.0
 */
@Data
public class UserDTO {
    private Long id;
    private String newPassword;
    private String oldPassword;
}
