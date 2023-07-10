package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/6 9:22 星期二
 * @Version 1.0
 */
public class Context {
    private IStrategy iStrategy;
    public Context(IStrategy iStrategy){
        this.iStrategy=iStrategy;
    }
    public FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO, JSONObject jsonObject, List<String> list, FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO){
       return  this.iStrategy.getUserActionRecordDetails(fileVideoLogRuleDetailsVO,jsonObject,list,fileVideoLogRuleDetailsDO);
    }
}
