package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/6 9:19 星期二
 * @Version 1.0
 */
public class SaveFileVerificationRules implements IStrategy{
    @Override
    public FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO,JSONObject jsonObject,List<String> list,FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO) {
        List<String> verificationNameList = new ArrayList<>();
        List<String> fileNameList = new ArrayList<>();
        JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.get("fileVerificationRulesDTO").toString());
        verificationNameList.add(jsonObject1.get("verificationName").toString());
        fileNameList.add(jsonObject1.get("firstFile").toString());
        fileNameList.add(jsonObject1.get("lastFile").toString());
        String verificationName = "规则名称: " + jsonObject1.get("verificationName").toString();
        String firstFile = "文件1: " + jsonObject1.get("firstFile").toString();
        String lastFile = "文件2: " + jsonObject1.get("lastFile").toString();
        String status ="";
        if("1".equals(jsonObject1.get("use").toString())){
            status ="状态: 启用";
        }else{
            status ="状态: 禁用";
        }
        fileVideoLogRuleDetailsVO.setBeforeStatus(status);
        list.add(verificationName);
        list.add(firstFile);
        list.add(lastFile);
        fileVideoLogRuleDetailsVO.setBeforeModificationRuleFileName(list);
        return fileVideoLogRuleDetailsVO;
    }
}
