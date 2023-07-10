package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/6 9:36 星期二
 * @Version 1.0
 */
public class UserLogin implements IStrategy{
    @Override
    public FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO, JSONObject jsonObject, List<String> list,FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO) {
        String InputContent ="输入内容: ";
        if(fileVideoLogRuleDetailsDO.getFailureReason()!=null){
            if(fileVideoLogRuleDetailsDO.getFailureReason().equals(ResultCodeEnum.USERNAME_ERROR.getMessage())) {
                fileVideoLogRuleDetailsVO.setFailureReason(ResultCodeEnum.USERNAME_ERROR.getMessage());
                list.add(InputContent+jsonObject.get("userName").toString());
            }else if(fileVideoLogRuleDetailsDO.getFailureReason().equals(ResultCodeEnum.PASSWORD_ERROR.getMessage())){
                fileVideoLogRuleDetailsVO.setFailureReason(ResultCodeEnum.PASSWORD_ERROR.getMessage());
                list.add(InputContent+jsonObject.get("password").toString());
            }
        }
        fileVideoLogRuleDetailsVO.setBeforeModificationRuleFileName(list);
        return fileVideoLogRuleDetailsVO;
    }
}
