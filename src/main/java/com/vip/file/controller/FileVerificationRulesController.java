package com.vip.file.controller;

import com.vip.file.aspect.LogTrack;
import com.vip.file.bean.PageData;
import com.vip.file.bean.Result;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.model.dto.FileVerificationRulesDTO;
import com.vip.file.model.dto.FileVerificationRulesDeleteDTO;
import com.vip.file.model.dto.NewFileVerificationRulesDTO;
import com.vip.file.model.query.FileVerificationRulesQuery;
import com.vip.file.model.vo.FileVerificationRulesVO;
import com.vip.file.service.FileVerificationRulesService;
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
 * @Date 2023/5/26 13:27 星期五
 * @Version 1.0
 */
@RestController
@Slf4j
@Api(tags = "文件名校验规则")
public class FileVerificationRulesController {
    @Autowired
    private FileVerificationRulesService fileVerificationRulesService;
    @ApiOperation(value = "文件名校验规则新增")
    @PostMapping("/saveFileVerificationRules")
    @LogTrack("后台管理系统,合成自定义,文件名校验规则自定义,新增")
    public Result<PageData> saveFileVerificationRules(@RequestBody NewFileVerificationRulesDTO fileVerificationRulesDTO) {
        //查询该规则名是否存在
       String name = fileVerificationRulesDTO.getVerificationName();
        fileVerificationRulesService.queryVerificationName(name);
       if(name != null){
           return Result.success(ResultCodeEnum.VERIFICATION_NAME_ERROR);
       }
       fileVerificationRulesService.saveFileVerificationRules(fileVerificationRulesDTO);
        return Result.success(ResultCodeEnum.SAVE_SUCCESS);
    }

    @ApiOperation(value = "文件名校验规则删除")
    @DeleteMapping("/deleteFileVerificationRules")
    @LogTrack("后台管理系统,合成自定义,文件名校验规则自定义,删除")
    public Result deleteFileVerificationRules(@RequestBody FileVerificationRulesDeleteDTO fileVerificationRulesDeleteDTO) {
        fileVerificationRulesService.deleteFileVerificationRules(fileVerificationRulesDeleteDTO.getId());
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
    @ApiOperation(value = "文件名校验规则修改")
    @PutMapping("/updateFileVerificationRules")
    @LogTrack("后台管理系统,合成自定义,文件名校验规则自定义,修改")
    public Result updateFileVerificationRules(@RequestBody FileVerificationRulesDTO fileVerificationRulesDTO) {
        fileVerificationRulesService.updateFileVerificationRules(fileVerificationRulesDTO.getNewFileVerificationRulesDTO());
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @ApiOperation(value = "文件名校验规则查询(分页)")
    @PostMapping("/queryFileVerificationRules")
    public Result<PageData> queryFileVerificationRules(@RequestBody FileVerificationRulesQuery fileVerificationRulesQuery ) {
        PageData pageData = fileVerificationRulesService.queryFileVerificationRules(fileVerificationRulesQuery);
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,pageData);
    }
    @ApiOperation(value = "文件名校验规则查询")
    @GetMapping("/queryAllFileVerificationRules")
    public Result<List<FileVerificationRulesVO>> queryAllFileVerificationRules(){
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,fileVerificationRulesService.queryAllFileVerificationRules());
    }
}
