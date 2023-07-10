package com.vip.file.model.dto;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/29 16:57 星期一
 * @Version 1.0
 */
@Data
public class FileVerificationRulesDTO {
    private NewFileVerificationRulesDTO newFileVerificationRulesDTO;
    private OldFileVerificationRulesDTO oldFileVerificationRulesDTO;
}
