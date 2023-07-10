package com.vip.file.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.aspect.LogTrack;
import com.vip.file.bean.Result;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.model.vo.CustomCompositionVO;
import com.vip.file.quartz.CustomCompositionSchedulingTask;
import com.vip.file.service.CustomCompositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zkc
 * @description
 * @Date 2023/5/23 13:31 星期二
 * @Version 1.0
 */
@RestController
@Slf4j
@Api(tags = "自定义合成")
public class CustomCompositionController {
    @Autowired
    private CustomCompositionSchedulingTask sampleScheduler;
    @Autowired
    private CustomCompositionService customCompositionService;
    @Autowired
    private Cache<String, Object> cache;

    @ApiOperation("修改合成并发数")
    @LogTrack(value = "后台管理系统,合成自定义,合成并发数设置,修改")
    @PutMapping("/updateCorePool")
    public Result updateCorePool(@RequestParam("oldCorePoll") Integer oldCorePoll,@RequestParam("corePool") Integer corePoll) {
        //查询数据库表中是否存在有数据
        CustomCompositionVO customCompositionVO = customCompositionService.queryCustomComposition();
        if (customCompositionVO == null || customCompositionVO.getCorePool() == null) {
            customCompositionService.saveCorePoolCronTime(corePoll, null,null);
        }
        customCompositionService.updateCorePool(corePoll, null,null);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @ApiOperation("修改定时合成时间")
    @LogTrack(value = "后台管理系统,合成自定义,定时合成设置,修改")
    @PutMapping("/updateCronTime")
    public Result updateCronTime(@RequestParam("oldCronTime") String oldCronTime,@RequestParam("cronTime") String cronTime) {
        String[] cronArray = cronTime.split(":");
        String cron = cronArray[2] + " " + cronArray[1] + " " + cronArray[0] + " * * ?";
        cache.put("cron",cron);
        cache.put("composeTime",cronTime);
        //修改定时任务时间
        sampleScheduler.updateCronExpression(cron);
        //查询数据库表中是否存在有数据
        CustomCompositionVO customCompositionVO = customCompositionService.queryCustomComposition();
        if (customCompositionVO == null || customCompositionVO.getCorePool() == null) {
            customCompositionService.saveCorePoolCronTime(null, cronTime,cron);
        }
        customCompositionService.updateCorePool(null, cronTime,cron);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @ApiOperation("定时合成、合成并发数查询")
    @GetMapping("/queryCustomComposition")
    public Result queryCustomComposition() {
        CustomCompositionVO customCompositionVO = customCompositionService.queryCustomComposition();
        customCompositionVO.setCron(customCompositionVO.getSynthesisTime());
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,customCompositionVO );
    }
    @ApiOperation("服务器最大合成并发数查询")
    @GetMapping("/queryMaxCorePool")
    public Result queryMaxCorePool() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int maxThreadCount = availableProcessors * 2;
        return Result.success(ResultCodeEnum.QUERY_SUCCESS,maxThreadCount );
    }
}
