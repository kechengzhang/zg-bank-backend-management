package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/25 15:32 星期日
 * @Version 1.0
 */
public class DeleteSplitVideo implements IStrategy{
    @Override
    public FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO, JSONObject jsonObject, List<String> list, FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO) {
        list.add("文件名: "+jsonObject.getJSONObject("param").getString("fileName"));
        fileVideoLogRuleDetailsVO.setBeforeModificationRuleFileName(list);
        return fileVideoLogRuleDetailsVO;
    }
}
