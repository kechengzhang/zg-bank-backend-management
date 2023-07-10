package com.vip.file.quartz;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.vip.file.model.entity.Files;
import com.vip.file.model.entity.UploadFileDo;
import com.vip.file.service.IFileService;
import com.vip.file.service.IUploadFileService;
import com.vip.file.utils.*;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import com.vip.file.websocket.service.MessagePushService;
import com.vip.file.websocket.service.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zkc
 * @description
 * @Date 2023/6/3 22:34 星期六
 * @Version 1.0
 */
@Slf4j
public class MergeVideoThread implements Runnable {
    private UploadFileDo file;
    private String currentTime;
    private OkHttpUtil okHttpUtil;
    private IUploadFileService uploadFileService;
    private MessagePushService messagePushService;
    private Integer composeTime;
    private String composeRules;
    private ConcurrentHashMap<String, Object> uploadFileMap;
    private ConcurrentHashMap<String, Object> composeStatusMap;
    private Cache<String, Object> cache;
    private IFileService fileService;
    private String composePath;

    public MergeVideoThread(UploadFileDo file,
                            String currentTime,
                            OkHttpUtil okHttpUtil,
                            IUploadFileService uploadFileService,
                            MessagePushService messagePushService,
                            Integer composeTime,
                            String composeRules,
                            ConcurrentHashMap<String, Object> uploadFileMap,
                            ConcurrentHashMap<String, Object> composeStatusMap,
                            Cache<String, Object> cache,IFileService fileService,String composePath) {
        this.file = file;
        this.currentTime = currentTime;
        this.okHttpUtil = okHttpUtil;
        this.uploadFileService = uploadFileService;
        this.messagePushService = messagePushService;
        this.composeTime = composeTime;
        this.composeRules = composeRules;
        this.uploadFileMap = uploadFileMap;
        this.composeStatusMap = composeStatusMap;
        this.cache = cache;
        this.fileService=fileService;
        this.composePath=composePath;
    }

    @Override
    public void run() {
        if (cache.getIfPresent("composePath") == null) {
            cache.put("composePath", composePath);
            cache.put("requestPath", ConstantEnum.REQUEST_PATH.getValue());
        }
        String id = file.getId();
        String ip = file.getIp();
        String port = file.getPort();
        String callBackUrl = "http://" + ip + ":" + port;
        String token = "";
        //获取token
        if (cache.getIfPresent("token") == null) {
            token = getToken(callBackUrl);
        } else {
            token = String.valueOf(cache.getIfPresent("token"));
        }
        //判断当前事件是否有文件正在上传，如果有，跳过本次视频合成
        if (uploadFileMap.containsKey(id)) {
            return;
        }
        //查看该事件分视频是否已到合成时间
//        Map<String, Object> map = uploadFileService.getFileByTargetIdAndTime(id);
//        if (map == null) {
//            return;
//        }
//        boolean flag = false;

        //创建时间加2分钟合成向后延迟2分钟
//        String compareTime = DateUtils.addDateMinut(composeRules, map.get("created_time").toString(), composeTime);
//        //判断是否需要合成，如果当前时间大于合成时间就需要合成，否则不需要合成
//        flag = DateUtils.compareTime(currentTime, compareTime);
//        if (flag) {
            mergeVideo(ip,token,callBackUrl,id);
            //需要合成
//            if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(token)) {
//                //开始合成，通知用户
//                setVideoFile(callBackUrl, token, id, "in process", "in process");
//            }
//            // webSocket 推送
//            messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.MERGE_VIDEO_ONGOING.getName(), id);
//            log.info("开始合成视频，通知页面:{}", id);
//            //合成视频存放路径
//            String composeVideoPath = cache.getIfPresent("composePath").toString() + DateUtils.dateTime();
//            File composeVideoFile = new File(composeVideoPath);
//            if (!composeVideoFile.exists()) {
//                composeVideoFile.mkdirs();
//            }
//            String path = DateUtils.dateTime() + "/" + UUID.randomUUID() + ".mp4";
//            String savePath = cache.getIfPresent("composePath").toString() + path;
//            String fileUrls = "";
//            boolean b = false;
//            //获取文件信息
//            List<Files> fileList = uploadFileService.selectFilesById(id);
//            //获取服务器ip
//            if (cache.getIfPresent("serverIp") == null) {
//                String serverIp = IpLongUtils.getServerIP();
//                cache.put("serverIp", serverIp);
//            }
//            String tempFileName = "";
//            for (int i = 0; i < fileList.size(); i++) {
//                StringBuilder stringBuilder = new StringBuilder();
//                String url = stringBuilder.append(cache.getIfPresent("serverIp")).append(ConstantEnum.SPLIT_VIDEO_FILE.getValue()).append(id).append("/").append(fileList.get(i).getFileName()).toString();
//                if (i == fileList.size() - 1) {
//                    fileUrls += url;
//                } else {
//                    tempFileName = url + ",";
//                    fileUrls += tempFileName;
//                }
//            }
//            composeStatusMap.put(id, true);
//            //视频合成
//            if (fileList.size() == 1) {
//                b = MergeVideoUtils.mergeVideo( cache.getIfPresent("composePath").toString(), path, ConstantEnum.FORMAT1.getValue(),fileList,fileService,cache);
//            } else {
//                b = MergeVideoUtils.mergeVideo( cache.getIfPresent("composePath").toString(), savePath, ConstantEnum.FORMAT.getValue(),fileList,fileService,cache);
//            }
//            if (b) {
//                String filePath = cache.getIfPresent("requestPath").toString() + path;
//                log.info("视频合成成功,合成视频访问路径:{}", filePath);
//                //合成成功修改数据库数据
//                file.setFilePath(filePath);
//                file.setStatus("0");
//                file.setIsCompose("1");
//                file.setSynthesisDate(DateUtils.getTime());
//                uploadFileService.updateUploadFile(file);
//                composeStatusMap.remove(id);
//                messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.MERGE_VIDEO_SUCCESS.getName(), id);
//                if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(token)) {
//                    //合成成功，通知用户
//                    setVideoFile(callBackUrl, token, id, cache.getIfPresent("composePath").toString() + path, fileUrls);
//                }
//            } else {
//                file.setSynthesisDate(DateUtils.getTime());
//                uploadFileService.updateUploadFile(file);
//            }
//        }
    }

    public void mergeVideo(String ip,String token,String callBackUrl,String id){
        //需要合成
        if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(token)) {
            //开始合成，通知用户
            setVideoFile(callBackUrl, token, id, "in process", "in process");
        }
        // webSocket 推送
//        messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.MERGE_VIDEO_ONGOING.getName(), id);
//        log.info("开始合成视频，通知页面:{}", id);
        //合成视频存放路径
        String composeVideoPath = cache.getIfPresent("composePath").toString() + DateUtils.dateTime();
        File composeVideoFile = new File(composeVideoPath);
        if (!composeVideoFile.exists()) {
            composeVideoFile.mkdirs();
        }
        String path = DateUtils.dateTime() + "/" + UUID.randomUUID() + ".mp4";
        String savePath = cache.getIfPresent("composePath").toString() + path;
        String fileUrls = "";
        boolean b = false;
        //获取文件信息
        List<Files> fileList = uploadFileService.selectFilesById(id);
        //获取服务器ip
        if (cache.getIfPresent("serverIp") == null) {
            String serverIp = IpLongUtils.getServerIP();
            cache.put("serverIp", serverIp);
        }
        String tempFileName = "";
        for (int i = 0; i < fileList.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            String url = stringBuilder.append(cache.getIfPresent("serverIp")).append(ConstantEnum.SPLIT_VIDEO_FILE.getValue()).append(id).append("/").append(fileList.get(i).getFileName()).toString();
            if (i == fileList.size() - 1) {
                fileUrls += url;
            } else {
                tempFileName = url + ",";
                fileUrls += tempFileName;
            }
        }
        composeStatusMap.put(id, true);
        //视频合成
        if (fileList.size() == 1) {
            b = MergeVideoUtils.mergeVideo( cache.getIfPresent("composePath").toString(), path, ConstantEnum.FORMAT1.getValue(),fileList,fileService,cache);
        } else {
            b = MergeVideoUtils.mergeVideo( cache.getIfPresent("composePath").toString(), savePath, ConstantEnum.FORMAT.getValue(),fileList,fileService,cache);
        }
        //视频合成成功所需操作
        if (b) {
            String filePath = cache.getIfPresent("requestPath").toString() + path;
            log.info("视频合成成功,合成视频访问路径:{}", filePath);
            //合成成功修改数据库数据
            file.setFilePath(filePath);
            file.setStatus("0");
            file.setIsCompose("1");
            file.setSynthesisDate(DateUtils.getTime());
            uploadFileService.updateUploadFile(file);
            composeStatusMap.remove(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fileId", id);
            jsonObject.put("isCompose", 3);
            jsonObject.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
            try {
                WebSocketServer.sendInfo(jsonObject.toJSONString());
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
            if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(token)) {
                //合成成功，通知用户
                setVideoFile(callBackUrl, token, id, cache.getIfPresent("composePath").toString() + path, fileUrls);
            }
        } else {
            file.setSynthesisDate(DateUtils.getTime());
            uploadFileService.updateUploadFile(file);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fileId", id);
            jsonObject.put("isCompose",4);
            jsonObject.put("type", ThirdInterfaceConstant.WebSocketMsg.VIDEO_COMPOSE.getName());
            try {
                WebSocketServer.sendInfo(jsonObject.toJSONString());
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
            composeStatusMap.remove(id);
        }
    }
    public String getToken(String callBackUrl) {
//        if(cache.getIfPresent("appKey") == null) {
//            cache.put("appKey",ConstantEnum.APP_KEY.getValue());
//            cache.put("appSecret",ConstantEnum.APP_SECRET.getValue());
//            cache.put("tokenUrl",ConstantEnum.TOKEN_URL.getValue());
//        }
////        String tokenUrl_ = callBackUrl+fileMergeVideoDTO.getTokenUrl()+"?appKey=%s&appSecret=%s";
//        String tokenUrl = callBackUrl+cache.getIfPresent("tokenUrl")+"?appKey=%s&appSecret=%s";
////        String queryTokenUrl = String.format(tokenUrl_,fileMergeVideoDTO.getAppKey(),fileMergeVideoDTO.getAppSecret());
//        String queryTokenUrl = String.format(tokenUrl,cache.getIfPresent("appKey"),cache.getIfPresent("appSecret"));
//        log.info("queryTokenUrl:{}",queryTokenUrl);
//        Map<String, String> header = new HashMap<>();
//        Map<String, Object> queryMap = new HashMap<>();
//        String tokenResult = okHttpUtil.postSend(queryTokenUrl, header, queryMap);
//        log.info("tokenResult：：：：：：："+tokenResult);
//        Map<String,Object> resultMap = JSONObject.parseObject(tokenResult, Map.class);
//        int code = Integer.parseInt(resultMap.get("code").toString());
//        if(code==200) {
//            Map<String, Object> dataMap = new HashMap<>();
//            if (resultMap.get("data") != null) {
//                dataMap = JSONObject.parseObject(resultMap.get("data").toString(), Map.class);
//                String token = dataMap.get("token").toString();
//                return token;
//            }
//
//        }else{
//            log.info("调用getAppToken获取token失败："+code+",msg:"+resultMap.get("msg"));
//            return null;
//        }
        return null;
    }

    public void setVideoFile(String callBackUrl, String token, String missionID, String sourceFile, String syntheticFile) {
//        if(cache.getIfPresent("setVideoFileUrl") == null) {
//            cache.put("setVideoFileUrl",ConstantEnum.SET_VIDEO_FILE_URL.getValue());
//        }
//        log.info("回调参数：：：missionID:{}", missionID, ",sourceFile:{}" + sourceFile, ",syntheticFile:{}" + syntheticFile);
//        Map<String, String> header = new HashMap<>();
//        Map<String, Object> queryMap = new HashMap<>();
////        String videoUrl = callBackUrl+fileMergeVideoDTO.getVideoFileUrl()+"?missionID=%s&sourceFile=%s&syntheticFile=%s";
//        String videoUrl = callBackUrl+cache.getIfPresent("setVideoFileUrl")+"?missionID=%s&sourceFile=%s&syntheticFile=%s";
//        String queryVideoFileUrl = String.format(videoUrl,missionID,sourceFile,syntheticFile);
//        log.info("queryVideoFileUrl：：：：：："+queryVideoFileUrl);
//        header.put("token",token);
//        String videoFileResult = okHttpUtil.postSend(queryVideoFileUrl, header, queryMap);
//        log.info("videoFileResult：：：：：：："+videoFileResult);
//        if(!StringUtils.isEmpty(videoFileResult)){
//            Map<String,Object> videoFileResultMap = JSONObject.parseObject(videoFileResult, Map.class);
//            int lastCode = Integer.parseInt(videoFileResultMap.get("code").toString());
//            log.info("事件"+missionID+"合成视频成功，回调结果:"+lastCode+",msg:"+videoFileResultMap.get("msg"));
//        }else{
//            log.info("回调VideoFileUrl失败，videoFileResult："+videoFileResult);
//        }
    }
}