package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/6 9:29 星期二
 * @Version 1.0
 */
public class UpdateFileVerificationRules implements IStrategy{
    @Override
    public FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO, JSONObject jsonObject, List<String> list,FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO) {
        String name="规则名称: ";
        String file1="文件1: ";
        String file2="文件2: ";
        //修改文件名校验
        JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.get("fileVerificationRulesDTO").toString());
        JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1.get("newFileVerificationRulesDTO").toString());
        JSONObject jsonObject3 = JSONObject.parseObject(jsonObject1.get("oldFileVerificationRulesDTO").toString());
        List<String> list1 = new ArrayList<>();
        String verificationName = name+ jsonObject3.get("verificationName").toString();
        String firstFile = file1 + jsonObject3.get("firstFile").toString();
        String lastFile = file2+ jsonObject3.get("lastFile").toString();
        list.add(verificationName);
        list.add(firstFile);
        list.add(lastFile);
        String beforeStatus ="状态: "+jsonObject3.get("status").toString();
        fileVideoLogRuleDetailsVO.setBeforeModificationRuleFileName(list);
        fileVideoLogRuleDetailsVO.setBeforeStatus(beforeStatus);
        String afterVerificationName = name + jsonObject2.get("verificationName").toString();
        String afterFirstFile = file1 + jsonObject2.get("firstFile").toString();
        String afterLastFile = file2 + jsonObject2.get("lastFile").toString();
        list1.add(afterVerificationName);
        list1.add(afterFirstFile);
        list1.add(afterLastFile);
        fileVideoLogRuleDetailsVO.setAfterModificationRuleFileName(list1);
        String afterStatus ="状态: "+jsonObject2.get("status").toString();
        fileVideoLogRuleDetailsVO.setAfterStatus(afterStatus);
        return fileVideoLogRuleDetailsVO;
    }
}
