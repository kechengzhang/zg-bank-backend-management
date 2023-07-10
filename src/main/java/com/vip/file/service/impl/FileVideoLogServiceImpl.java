package com.vip.file.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.vip.file.bean.PageData;
import com.vip.file.mapper.FileVideoLogMapper;
import com.vip.file.model.dto.FileUploadFailedLogDTO;
import com.vip.file.model.entity.FileLogDO;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.query.FileVideoLogQuery;
import com.vip.file.model.vo.FileUploadFailedVO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;
import com.vip.file.service.FileVideoLogService;
import com.vip.file.service.strategy.Context;
import com.vip.file.service.strategy.UserActionRecordFactory;
import com.vip.file.utils.CommonUtil;
import com.vip.file.utils.ConstantEnum;
import com.vip.file.utils.DateUtils;
import com.vip.file.utils.IpLongUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/29 22:32 星期一
 * @Version 1.0
 */
@Service
public class FileVideoLogServiceImpl implements FileVideoLogService {
    @Autowired
    private FileVideoLogMapper fileVideoLogMapper;

    @Override
    public int saveFileVideoLog(FileLogDO fileLogDO) {
        return fileVideoLogMapper.saveFileVideoLog(fileLogDO);
    }

    @Override
    public PageData queryUserActionRecord(FileVideoLogQuery fileVideoLogQuery) {
        PageHelper.startPage(fileVideoLogQuery.getPageNumber(), fileVideoLogQuery.getPageSize());
        PageData pageData = new PageData(new PageInfo(fileVideoLogMapper.queryUserActionRecord(fileVideoLogQuery)));
        return pageData;
    }

    @Override
    public FileVideoLogRuleDetailsVO queryUserActionRecordDetails(Long id) {
        FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO = new FileVideoLogRuleDetailsVO();
        FileVideoLogRuleDetailsDO fileVideoLogRuleDetailsDO = fileVideoLogMapper.queryUserActionRecordDetails(id);
        //获取ip地址
        fileVideoLogRuleDetailsVO.setIpAddress(IpLongUtils.long2Ip((Long.parseLong(fileVideoLogRuleDetailsDO.getIpAddress()))));
       //操作类型
        fileVideoLogRuleDetailsVO.setOperationType(fileVideoLogRuleDetailsDO.getOperationType());
        //请求参数
        JSONObject jsonObject = JSONObject.parseObject(fileVideoLogRuleDetailsDO.getRequestParameter());
        //类型名称判断使用
        String type = fileVideoLogRuleDetailsDO.getOperationObjects() + fileVideoLogRuleDetailsDO.getOperationType();
        List<String> list = new ArrayList<>();
        if (type.equals(ConstantEnum.FILE_VERIFICATION_RULES_SAVE.getValue())) {
            Context context =new Context(UserActionRecordFactory.getIStrategy(1));
            fileVideoLogRuleDetailsVO = context.getUserActionRecordDetails(fileVideoLogRuleDetailsVO, jsonObject,list,  fileVideoLogRuleDetailsDO);
        } else if (type.equals(ConstantEnum.FILE_VERIFICATION_RULES_UPDATE.getValue())) {
            Context context =new Context(UserActionRecordFactory.getIStrategy(2));
            fileVideoLogRuleDetailsVO = context.getUserActionRecordDetails(fileVideoLogRuleDetailsVO, jsonObject,list,  fileVideoLogRuleDetailsDO);
        } else if (type.equals(ConstantEnum.FILE_VERIFICATION_RULES_DELETE.getValue())|| type.equals(ConstantEnum.FILE_ALL_VERIFICATION_RULES_DELETE.getValue())) {
            Context context =new Context(UserActionRecordFactory.getIStrategy(3));
            fileVideoLogRuleDetailsVO = context.getUserActionRecordDetails(fileVideoLogRuleDetailsVO, jsonObject,list,  fileVideoLogRuleDetailsDO);
        } else if (type.equals(ConstantEnum.SYNTHETIC_VIDEO_DELETE.getValue())|| type.equals(ConstantEnum.SYNTHETIC_ALL_VIDEO_DELETE.getValue())) {
            Context context = new Context(UserActionRecordFactory.getIStrategy(4));
            fileVideoLogRuleDetailsVO = context.getUserActionRecordDetails(fileVideoLogRuleDetailsVO, jsonObject, list, fileVideoLogRuleDetailsDO);
                    } else if (type.equals(ConstantEnum.PASSWORD_UPDATE.getValue()) || type.equals(ConstantEnum.CUSTOM_COMPOSITION_UPDATE.getValue()) || type.equals(ConstantEnum.CUSTOM_COMPOSITION_CORE_UPDATE.getValue())) {
            Context context =new Context(UserActionRecordFactory.getIStrategy(5));
            fileVideoLogRuleDetailsVO = context.getUserActionRecordDetails(fileVideoLogRuleDetailsVO, jsonObject,list,  fileVideoLogRuleDetailsDO);
        }else if (type.equals(ConstantEnum.USER_LOGIN.getValue())){
            Context context =new Context(UserActionRecordFactory.getIStrategy(6));
            fileVideoLogRuleDetailsVO = context.getUserActionRecordDetails(fileVideoLogRuleDetailsVO, jsonObject,list,  fileVideoLogRuleDetailsDO);
        }else if(type.equals(ConstantEnum.FILE_UPLOAD.getValue()) || type.equals(ConstantEnum.FILE_NAME_CHECK.getValue())){
            Context context =new Context(UserActionRecordFactory.getIStrategy(7));
            fileVideoLogRuleDetailsVO = context.getUserActionRecordDetails(fileVideoLogRuleDetailsVO, jsonObject,list,  fileVideoLogRuleDetailsDO);
        }else if(type.equals(ConstantEnum.SPLIT_VIDEO_DELETE.getValue())){
            Context context =new Context(UserActionRecordFactory.getIStrategy(8));
            fileVideoLogRuleDetailsVO = context.getUserActionRecordDetails(fileVideoLogRuleDetailsVO, jsonObject,list,  fileVideoLogRuleDetailsDO);
        }
        return fileVideoLogRuleDetailsVO;
    }

    @Override
    public List<FileUploadFailedVO> queryUploadFailedFile() {
        return fileVideoLogMapper.queryUploadFailedFile();
    }

    @Override
    public int updateUploadFailedFile() {
        return fileVideoLogMapper.updateUploadFailedFile();
    }

    @Override
    public int saveUploadFailedLog(FileUploadFailedLogDTO fileUploadFailedLogDTO, HttpServletRequest httpServletRequest) {
          FileLogDO logDO = new FileLogDO();
          logDO.setSystemName("前端系统");
          //操作模块
          logDO.setBusinessModule("上传分视频");
          //操作对象
          logDO.setOperationObjects("文件名校验");
          logDO.setOperationType("上传");
          //获取请求ip
          logDO.setIp(IpLongUtils.ip2Long(CommonUtil.getIpAddress(httpServletRequest)));
          logDO.setCreateTime(DateUtils.getTime());
          Gson gson = new Gson();
          String json = gson.toJson(fileUploadFailedLogDTO);
          logDO.setRequestParameter(json);
          logDO.setOperationResult("失败");
          logDO.setFailureReason(fileUploadFailedLogDTO.getMsg());
        //保存日志
        fileVideoLogMapper.saveFileVideoLog(logDO);
        return 1;
    }

    /**
     * 删除接口数据处理
     */
    public List<String> deleteDataDispose(JSONObject jsonObject) {
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("name").toString());
        List<String> newVerificationNameList = jsonArray.toJavaList(String.class);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < newVerificationNameList.size(); i++) {
            String fileName = "文件名" + i+1 + ": " + newVerificationNameList.get(i);
            list.add(fileName);
        }
        return list;
    }
}
