package com.vip.file.quartz;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.model.entity.UploadFileDo;
import com.vip.file.model.vo.CustomCompositionVO;
import com.vip.file.service.CustomCompositionService;
import com.vip.file.service.IFileService;
import com.vip.file.service.IUploadFileService;
import com.vip.file.service.impl.CustomCompositionServiceImpl;
import com.vip.file.utils.DateUtils;
import com.vip.file.utils.OkHttpUtil;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import com.vip.file.websocket.service.MessagePushService;
import com.vip.file.websocket.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author zkc
 * @description
 * @Date 2023/6/6 15:39 星期二
 * @Version 1.0
 */
@Component
public class CustomCompositionSchedulingTask {
    @Autowired
    private Cache<String, Object> cache;
    @Autowired
    private CustomCompositionService customCompositionService;
    @Autowired
    private IUploadFileService uploadFileService;
    @Autowired
    private MessagePushService messagePushService;
    @Autowired
    private OkHttpUtil okHttpUtil;
    @Value("${file.compose-path}")
    private String composePath;
    @Value("${compose.compose_time}")
    private Integer composeTime;
    @Value("${compose.compose_rules}")
    private String composeRules;
    @Value("${ffmpeg.folderPath}")
    private String folderPath;
    @Value("${ffmpeg.resourcePath}")
    private String resourcePath;
    public static ConcurrentHashMap<String , Object> uploadFileMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String , Object> composeStatusMap = new ConcurrentHashMap<>();
    private final CustomCompositionServiceImpl customCompositionServiceImpl;
    public CustomCompositionSchedulingTask(CustomCompositionServiceImpl customCompositionServiceImpl) {
        this.customCompositionServiceImpl = customCompositionServiceImpl;
    }
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private CustomCompositionSchedulingTask sampleTask;
    @Autowired
    private IFileService fileService;
    private ScheduledFuture<?> scheduledFuture;

    /**
     * 初始化方法中调用更新定时任务表达式的方法
     *
     */
    @PostConstruct
    public void init() {
        CustomCompositionVO customCompositionVO = customCompositionService.queryCustomComposition();
        cache.put("composeTime",customCompositionVO.getSynthesisTime());
        updateCronExpression(customCompositionVO.getCron());
    }

    /**
     *视频合成
     *
     */
    @Scheduled(cron = "#{@customCompositionServiceImpl.getCronTime()}")
    public void runTask() {
        // 执行定时任务的逻辑
        int corePool = 1;
        if (cache.getIfPresent("corePool") != null) {
            corePool = (int) cache.getIfPresent("corePool");
        } else {
            CustomCompositionVO customCompositionVO = customCompositionService.queryCustomComposition();
            corePool = customCompositionVO.getCorePool();
            cache.put("corePool", corePool);
            cache.put("folderPath", folderPath);
            cache.put("resourcePath", resourcePath);
        }
        UploadFileDo uploadFile = new UploadFileDo();
        //获取所有未合成视频
        List<UploadFileDo> uploadFiles = uploadFileService.selectUploadFileList(uploadFile);
        String currentTime = DateUtils.getTime();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePool, 20,
                1000, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for(UploadFileDo uploadFileDo: uploadFiles){
            // 通知前端，打开添加视频页面提示视频合成中
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fileId", uploadFileDo.getId());
            jsonObject.put("isCompose", 0);
            jsonObject.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
            try {
                WebSocketServer.sendInfo(jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            MergeVideoThread mergeVideoThread = new MergeVideoThreadBuilder()
                    .setCurrentTime(currentTime)
                    .setOkHttpUtil(okHttpUtil)
                    .setIUploadFileService(uploadFileService)
                    .setMessagePushService(messagePushService)
                    .setComposeTime(composeTime)
                    .setComposeRules(composeRules)
                    .setUploadFileMap(uploadFileMap)
                    .setComposeStatusMap(composeStatusMap)
                    .setCache(cache)
                    .setFile(uploadFileDo)
                    .setFileService(fileService)
                    .setComposePath(composePath).
                            build();
            executor.execute(mergeVideoThread);
        }
        executor.shutdown();
    }
    public void updateCronExpression(String newCronExpression) {
        // 取消当前的定时任务
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        // 重新创建定时任务
        scheduledFuture = taskScheduler.schedule(sampleTask::runTask, new CronTrigger(newCronExpression));
    }
}
