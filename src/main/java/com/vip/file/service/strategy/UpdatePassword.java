package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/6 9:34 星期二
 * @Version 1.0
 */
public class UpdatePassword implements IStrategy{
    @Override
    public FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO, JSONObject jsonObject, List<String> list,FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO) {
        if(fileVideoLogRuleDetailsDO.getFailureReason()!=null){
//            fileVideoLogRuleDetailsVO.setBeforeModificationRuleFileName(list);
//            fileVideoLogRuleDetailsVO.setAfterModificationRuleFileName(list);
            fileVideoLogRuleDetailsVO.setFailureReason(fileVideoLogRuleDetailsDO.getFailureReason());
        }
        String beforeUpdate = "修改前: ";
        String afterUpdate = "修改后: ";
        if (jsonObject.containsKey("userDTO")) {
            if(fileVideoLogRuleDetailsVO.getFailureReason() == null){
                JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.get("userDTO").toString());
                beforeUpdate = beforeUpdate + jsonObject1.get("oldPassword").toString();
                afterUpdate = afterUpdate + jsonObject1.get("newPassword").toString();
            }else{
                return fileVideoLogRuleDetailsVO;
            }
        } else {
            if (jsonObject.containsKey("cronTime")) {
                beforeUpdate = beforeUpdate + jsonObject.get("cronTime").toString();
                afterUpdate = afterUpdate + jsonObject.get("oldCronTime").toString();
            } else {
                beforeUpdate = beforeUpdate + jsonObject.get("oldCorePoll").toString();
                afterUpdate = afterUpdate+ jsonObject.get("corePoll").toString();
            }
        }
        list.add(beforeUpdate);
        list.add(afterUpdate);
        fileVideoLogRuleDetailsVO.setBeforeModificationRuleFileName(list);
        return fileVideoLogRuleDetailsVO;
    }
}
