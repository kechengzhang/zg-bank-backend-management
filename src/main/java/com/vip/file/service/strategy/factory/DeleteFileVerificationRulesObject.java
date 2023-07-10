package com.vip.file.service.strategy.factory;

import com.vip.file.service.strategy.DeleteFileVerificationRules;
import com.vip.file.service.strategy.IStrategy;

/**
 * @author zkc
 * @description
 * @Date 2023/7/10 16:55 星期一
 * @Version 1.0
 */
public class DeleteFileVerificationRulesObject implements  UserActionRecordFactory1{
    @Override
    public IStrategy getStrategyObject() {
        return new DeleteFileVerificationRules();
    }
}
