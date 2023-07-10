package com.vip.file.controller;

import com.vip.file.aspect.LogTrack;
import com.vip.file.bean.PageData;
import com.vip.file.bean.Result;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.model.dto.FileVerificationRulesDeleteDTO;
import com.vip.file.model.dto.SyntheticVideoDTO;
import com.vip.file.model.query.SyntheticVideoFailureQuery;
import com.vip.file.model.query.SyntheticVideoQuery;
import com.vip.file.model.vo.BankBranchVO;
import com.vip.file.model.vo.SyntheticVideoFailureVO;
import com.vip.file.service.SyntheticVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 9:39 星期五
 * @Version 1.0
 */
@RestController
@Api(tags = "合成视频")
public class SyntheticVideoController {
    @Autowired
    private SyntheticVideoService syntheticVideoService;
    @ApiOperation(value = "合成视频列表查询")
    @PostMapping("/querySyntheticVideo")
    public Result<PageData> querySyntheticVideo(@RequestBody SyntheticVideoQuery syntheticVideoQuery) {
        PageData pageData =syntheticVideoService.querySyntheticVideo(syntheticVideoQuery);
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,pageData);
    }

    @ApiOperation(value = "合成视频删除")
    @DeleteMapping("/deleteSyntheticVideo")
    @LogTrack("后台管理系统,合成视频管理,合成视频列表,删除")
    public Result deleteSyntheticVideo(@RequestBody SyntheticVideoDTO syntheticVideoDTO) {
        syntheticVideoService.deleteSyntheticVideo(syntheticVideoDTO);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
    @ApiOperation(value = "合成失败视频查询")
    @PostMapping("/querySyntheticVideoFailure")
    public Result<PageData> querySyntheticVideoFailure(@RequestBody SyntheticVideoFailureQuery syntheticVideoFailureQuery) {
        PageData pageData = syntheticVideoService.querySyntheticVideoFailure(syntheticVideoFailureQuery);
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,pageData);
    }
    @ApiOperation(value = "获取合成视频")
    @GetMapping("/queryVideoCompose")
    public Result<List<String>> queryVideoCompose() {
        List<String>list = syntheticVideoService.queryVideoCompose();
        if(list.isEmpty()){
            return Result.success(ResultCodeEnum.QUERY_VIDEO_COMPOSE_SUCCESS);
        }
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,list);
    }

    @ApiOperation(value = "银行信息查询")
    @GetMapping("/queryBankInformation")
    public Result<List<BankBranchVO>> queryBankInformation(@RequestParam(value = "bankBranchName",required = false) String bankBranchName) {
        List<BankBranchVO>list = syntheticVideoService.queryBankInformation(bankBranchName);
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,list);
    }
}
