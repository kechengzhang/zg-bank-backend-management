package com.vip.file.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.exception.SqlException;
import com.vip.file.mapper.CustomCompositionMapper;
import com.vip.file.model.vo.CustomCompositionVO;
import com.vip.file.service.CustomCompositionService;
import com.vip.file.utils.DateUtils;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import com.vip.file.websocket.service.MessagePushService;
import com.vip.file.websocket.service.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zkc
 * @description
 * @Date 2023/5/23 14:05 星期二
 * @Version 1.0
 */
@Service
@Slf4j
public class CustomCompositionServiceImpl implements CustomCompositionService {
    @Autowired
    private CustomCompositionMapper customCompositionMapper;
    @Autowired
    private Cache<String, Object> cache;
    @Value("${compose.compose_time}")
    private Integer composeTime;
    @Autowired
    private MessagePushService messagePushService;
    @Override
    public int updateCorePool(Integer corePool, String synthesisTime,String cronTime) {
        try {
            customCompositionMapper.updateCorePool(corePool,synthesisTime,cronTime);
              JSONObject jsonObject = new JSONObject();
//             String beginTime = DateUtils.reduceDateMinute(synthesisTime,composeTime);
//            jsonObject.put("beginTime",DateUtils.getDate()+" "+beginTime);
//            jsonObject.put("endTime",DateUtils.getDate()+" "+synthesisTime);
//            jsonObject.put("currentTime",DateUtils.getTime());
            jsonObject.put("isCompose",3);
            jsonObject.put("type",ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
            WebSocketServer.sendInfo1(jsonObject.toJSONString());
        } catch (Exception e) {
         log.error(e.getMessage(),e);
         throw new SqlException("SQL异常");
        }
        return 1;
    }

    @Override
    public CustomCompositionVO queryCustomComposition() {
        return customCompositionMapper.queryCustomComposition();
    }

    @Override
    public int saveCorePoolCronTime(Integer corePoll, String synthesisTime,String cronTime) {
        return customCompositionMapper.saveCorePoolCronTime(corePoll,synthesisTime,cronTime);
    }

    @Override
    public String getCronTime() {
        String cron ="";
        //查询cron表达式
        CustomCompositionVO customCompositionVO = customCompositionMapper.queryCustomComposition();
        if(customCompositionVO != null) {
            cron = customCompositionVO.getCron();
            cache.put("cron", customCompositionVO.getCron());
        }
        return cron;
    }
}
