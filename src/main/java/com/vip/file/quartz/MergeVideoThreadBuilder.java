package com.vip.file.quartz;

import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.model.entity.UploadFileDo;
import com.vip.file.service.IFileService;
import com.vip.file.service.IUploadFileService;
import com.vip.file.utils.OkHttpUtil;
import com.vip.file.websocket.service.MessagePushService;

import javax.print.DocFlavor;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zkc
 * @description
 * @Date 2023/6/27 15:49 星期二
 * @Version 1.0
 */
public class MergeVideoThreadBuilder {
    private UploadFileDo file;
    private String currentTime;
    private OkHttpUtil okHttpUtil;
    private IUploadFileService uploadFileService;
    private MessagePushService messagePushService;
    private Integer composeTime;
    private String composeRules;
    private ConcurrentHashMap<String , Object> uploadFileMap ;
    private ConcurrentHashMap<String , Object> composeStatusMap;
    private Cache<String, Object> cache;
    private IFileService fileService;
    private String composePath;


    public MergeVideoThreadBuilder setFile(UploadFileDo file) {
        this.file = file;
        return this;
    }
    public MergeVideoThreadBuilder setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
        return this;
    }
    public MergeVideoThreadBuilder setOkHttpUtil(OkHttpUtil okHttpUtil) {
        this.okHttpUtil = okHttpUtil;
        return this;
    }
    public MergeVideoThreadBuilder setIUploadFileService(IUploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
        return this;
    }
    public MergeVideoThreadBuilder setMessagePushService(MessagePushService messagePushService) {
        this.messagePushService = messagePushService;
        return this;
    }
    public MergeVideoThreadBuilder setComposeTime(Integer composeTime) {
        this.composeTime = composeTime;
        return this;
    }
    public MergeVideoThreadBuilder setComposeRules(String composeRules) {
        this.composeRules = composeRules;
        return this;
    }
    public MergeVideoThreadBuilder setUploadFileMap(ConcurrentHashMap<String , Object> uploadFileMap) {
        this.uploadFileMap = uploadFileMap;
        return this;
    }
    public MergeVideoThreadBuilder setComposeStatusMap(ConcurrentHashMap<String , Object> composeStatusMap) {
        this.composeStatusMap = composeStatusMap;
        return this;
    }
    public MergeVideoThreadBuilder setCache(Cache<String, Object> cache) {
        this.cache = cache;
        return this;
    }
    public MergeVideoThreadBuilder setFileService(IFileService fileService) {
        this.fileService = fileService;
        return this;
    }
    public MergeVideoThreadBuilder setComposePath(String composePath) {
        this.composePath = composePath;
        return this;
    }

    public MergeVideoThread build() {
            return new MergeVideoThread(file, currentTime,
          okHttpUtil, uploadFileService, messagePushService, composeTime, composeRules,
                    uploadFileMap , composeStatusMap, cache,fileService,composePath);
    }


}
