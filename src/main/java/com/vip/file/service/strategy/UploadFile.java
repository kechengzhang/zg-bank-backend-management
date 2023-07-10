package com.vip.file.service.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/6/25 14:34 星期日
 * @Version 1.0
 */
public class UploadFile implements IStrategy{
    @Override
    public FileVideoLogRuleDetailsVO getUserActionRecordDetails(FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO, JSONObject jsonObject, List<String> list, FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO) {
        List<String> allList = new ArrayList<>();
        int j =1;
        if("失败".equals(fileVideoLogRuleDetailsDO.getOperationResult())){
            JSONArray fileNames = JSONObject.parseArray(jsonObject.getString("file"));

            for(int i=0;i<fileNames.size();i++){
                j=j+i;
                String fileName="文件"+j+": "+fileNames.get(i);
                allList.add(fileName);
            }
            String failureReason = fileVideoLogRuleDetailsDO.getFailureReason();
            fileVideoLogRuleDetailsVO.setFailureReason(failureReason);

        }else{
            JSONObject json= JSON.parseObject(jsonObject.get("param").toString());
            String []  jsonArray = json.getString("fileNames").split(",");
            for(int i=0;i<jsonArray.length;i++){
                j=j+i;
                String fileName="文件"+j+": "+jsonArray[i];
                allList.add(fileName);
            }
        }
        fileVideoLogRuleDetailsVO.setBeforeModificationRuleFileName(allList);
        return fileVideoLogRuleDetailsVO;
    }
}
