package com.vip.file.controller;

import com.vip.file.bean.PageData;
import com.vip.file.bean.Result;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.model.dto.FileUploadFailedLogDTO;
import com.vip.file.model.query.FileVideoLogQuery;
import com.vip.file.model.vo.FileUploadFailedVO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;
import com.vip.file.service.FileVideoLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/30 9:11 星期二
 * @Version 1.0
 */
@RestController
@Slf4j
@Api(tags = "用户操作记录")
public class FileVideoLogController {
    @Autowired
    private FileVideoLogService fileVideoLogService;
    @ApiOperation(value = "用户操作记录查询")
    @PostMapping("/queryUserActionRecord")
    public Result<PageData> queryUserActionRecord(@RequestBody FileVideoLogQuery fileVideoLogQuery) {
        PageData pageData=fileVideoLogService.queryUserActionRecord(fileVideoLogQuery);
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,pageData);
    }
    @ApiOperation(value = "用户操作记录详情查询")
    @GetMapping("/queryUserActionRecord")
    public Result<FileVideoLogRuleDetailsVO> queryUserActionRecordDetails(@RequestParam Long id) {
        FileVideoLogRuleDetailsVO fileVideoLogRuleDetailsVO = fileVideoLogService.queryUserActionRecordDetails(id);
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,fileVideoLogRuleDetailsVO);
    }
    @ApiOperation(value = "上传失败文件查询查询")
    @GetMapping("/queryUploadFailedFile")
    public Result<List<FileUploadFailedVO>> queryUploadFailedFile() {
        List<FileUploadFailedVO> list = fileVideoLogService.queryUploadFailedFile();
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,list);
    }

    @ApiOperation(value = "上传失败文件修改")
    @PutMapping("/updateUploadFailedFile")
    public Result updateUploadFailedFile() {
        fileVideoLogService.updateUploadFailedFile();
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }
    @ApiOperation(value = "文件名校验失败日志保存")
    @PostMapping("/saveUploadFailedLog")
    public Result saveUploadFailedLog(@RequestBody FileUploadFailedLogDTO fileUploadFailedLogDTO, HttpServletRequest httpServletRequest) {
        fileVideoLogService.saveUploadFailedLog(fileUploadFailedLogDTO,httpServletRequest);
        return Result.success(ResultCodeEnum.SAVE_SUCCESS);
    }
}
