package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/6 9:30 星期二
 * @Version 1.0
 */
public class DeleteFileVerificationRules implements IStrategy{
    @Override
    public FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO, JSONObject jsonObject, List<String> list,FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO) {
        JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.get("fileVerificationRulesDeleteDTO").toString());
        list = deleteDataDispose(jsonObject1);
        fileVideoLogRuleDetailsVO.setBeforeModificationRuleFileName(list);
        return fileVideoLogRuleDetailsVO;
    }
    /**
     * 删除接口数据处理
     */
    public List<String> deleteDataDispose(JSONObject jsonObject) {
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("name").toString());
        List<String> newVerificationNameList = jsonArray.toJavaList(String.class);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < newVerificationNameList.size(); i++) {
            int result=i+1;
            String fileName = "规则名" + result + ": " + newVerificationNameList.get(i);
            list.add(fileName);
        }
        return list;
    }
}
