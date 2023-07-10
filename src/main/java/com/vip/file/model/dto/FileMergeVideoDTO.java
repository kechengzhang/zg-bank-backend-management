package com.vip.file.model.dto;

import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.model.entity.UploadFileDo;
import com.vip.file.service.IUploadFileService;
import com.vip.file.utils.OkHttpUtil;
import com.vip.file.websocket.service.MessagePushService;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zkc
 * @description
 * @Date 2023/6/5 10:24 星期一
 * @Version 1.0
 */
@Data
public class FileMergeVideoDTO {
    private UploadFileDo file;
    private String tempId;
    private String token;
    private String callBackUrl ;
    private SimpleDateFormat simpleDateFormat ;
    private String currentTime;
    private OkHttpUtil okHttpUtil;
//    private String savePath;
//    private String confFilePath;
//    private String requestPath;
//    private String composePath;
//    private String serverPath;
    private IUploadFileService uploadFileService;
    private MessagePushService messagePushService;
    private Integer composeTime;
    private String composeRules;
//    private String appKey;
//    private String appSecret;
//    private String tokenUrl;
//    private String videoFileUrl;
    private ConcurrentHashMap<String , Object> uploadFileMap ;
    private ConcurrentHashMap<String , Object> composeStatusMap;
    private Cache<String, Object> cache;
}
