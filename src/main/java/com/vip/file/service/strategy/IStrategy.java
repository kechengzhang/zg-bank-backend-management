package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.List;

/**
 * @author zkc
 * 获取日志详情
 *
 */
public interface IStrategy {
    /**
     *
     *获取用户操作记录详情
     *
     * @param fileVideoLogRuleDetailsVO
     * @param list
     * @param jsonObject
     * @param fileVideoLogRuleDetailsDO
     * @return
     */
    FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO, JSONObject jsonObject, List<String> list,FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO);
}
