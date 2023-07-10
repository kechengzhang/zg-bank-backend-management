package com.vip.file.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.vip.file.aspect.LogTrack;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.common.Convert;
import com.vip.file.common.Page;
import com.vip.file.model.dto.UploadFileDto;
import com.vip.file.service.IFileService;
import com.vip.file.service.IUploadFileService;
import com.vip.file.utils.*;
import com.vip.file.websocket.service.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 【上传文件】Controller
 *
 * @author zs
 * @date 2022-06-27
 */
@Slf4j
@Api(tags = "【上传文件】Controller")
@Controller
@RequestMapping("/system/file")
public class UploadFileController {
    private String prefix = "system/file";

    @Autowired
    private OkHttpUtil okHttpUtil;
    @Value("${file.save-path:/data-center/files/vip-file-manager}")
    private String savePath;
    @Value("${file.conf-path:/data-center/files/vip-file-manager/conf}")
    private String confFilePath;
    @Autowired
    private IUploadFileService uploadFileService;
    @Autowired
    private IFileService fileService;

    /**
     * 查询【上传文件】列表
     */
    @ApiOperation(value = "查询【上传文件】列表")
    @PostMapping("/listPage")
    @ResponseBody
    public ResponseEntity<Result> list(@RequestBody JSONObject param) {
        try {
            if (!param.containsKey("page") || StringUtils.isEmpty(param.getJSONObject("page").getString("currentPage")) || StringUtils.isEmpty(param.getJSONObject("page").getString("perPageSize"))) {
                return ResultUtil.error("分页参数错误");
            }
            Page page = JSONObject.toJavaObject(param.getJSONObject("page"), Page.class);
            List<Map<String, Object>> list = uploadFileService.selectUploadFilePage(page);
            for (Map<String, Object> stringObjectMap : list) {
                String occurTime = stringObjectMap.get("occurTime").toString();
                String substring = occurTime.substring(0, occurTime.lastIndexOf("."));
                stringObjectMap.put("occurTime", substring);
            }
            PageInfo<Map<String, Object>> info = new PageInfo<Map<String, Object>>();
            if (list != null && list.size() > 0) {
                info = new PageInfo<Map<String, Object>>(list);
            }
            return ResultUtil.success(info);
        } catch (Exception e) {
            log.error(CommonUtil.getErrorMessage(Thread.currentThread().getStackTrace()[1], e));
            return ResultUtil.exception("获取文件(分页)异常");
        }
    }


    @ApiOperation(value = "视频详情查询")
    @GetMapping("/video")
    @ResponseBody
    public ResponseEntity<Result> video(@RequestParam String id) {
        //所有分视频列表
        List<Map<String, Object>> list = uploadFileService.selectvideoFilePage(id);
        //查看合成视频列表
        List<Map<String, Object>> fileList = uploadFileService.selectFileById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("filePath", list);
        jsonObject.put("file", fileList);
        return ResultUtil.success(jsonObject);

    }



    @ApiOperation(value = "修改总视频")
    @PostMapping("/editUploadFile")
    @ResponseBody
    public ResponseEntity<Result> editUploadFile(@Valid @RequestBody UploadFileDto uploadFileDto) {
        return ResultUtil.success(uploadFileService.updateUploadFile(uploadFileDto));
    }

//
//    @Resource
//    DasysTaskTimer dt;


    /**
     * 删除【上传文件】
     */
    @ApiOperation(value = "删除【上传文件】")
    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Result> remove(@RequestBody String ids) {
        return ResultUtil.success(uploadFileService.deleteUploadFileByIds(ids));
    }


    @ApiOperation(value = "删除总视频：1总行2分行")
    @PostMapping("/removeBank")
    @ResponseBody
    public ResponseEntity<Result> removeBank(@RequestBody JSONObject param) {
        String bank = param.getString("bank");
        try {
            if ("1".equals(param.getString(bank))) {
                return ResultUtil.success("总行无法删除/修改视频信息");
            } else {
                String id = param.getString("id");
                uploadFileService.deleteUploadFileById(id);
                return ResultUtil.success("删除成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResultUtil.error("删除失败");
        }

    }


//    @ApiOperation(value = "删除分视频：1总行2分行")
//    @PostMapping("/removeVideo")
//    @ResponseBody
//    public ResponseEntity<Result> removeVideo(@RequestBody JSONObject param) {
//        String bank = param.getString("bank");
//        String key = "1";
//        try {
//            if (key.equals(param.getString(bank))) {
//                return ResultUtil.success("总行无法删除/修改视频信息");
//            } else {
//                String id = param.getString("id");
//                uploadFileService.deleteFileById(id);
//                return ResultUtil.success(ResultCodeEnum.DELETE_SUCCESS.getMessage());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResultUtil.error("删除失败");
//
//        }
//    }
    @ApiOperation(value = "删除分视频：1总行2分行")
    @LogTrack(value = "前端系统,上传分视频,删除分视频文件,删除")
    @PostMapping("/removeVideo")
    @ResponseBody
    public  com.vip.file.bean.Result removeVideo(@RequestBody JSONObject param) {
        String bank = param.getString("bank");
        try {
            if ("1".equals(param.getString(bank))) {
                return com.vip.file.bean.Result.failure(ResultCodeEnum.VIDEO_DELETE_ERROR);
            } else {
                String id = param.getString("id");
                uploadFileService.deleteFileById(id);
                return com.vip.file.bean.Result.success(ResultCodeEnum.DELETE_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return com.vip.file.bean.Result.failure(ResultCodeEnum.DELETE_ERROR);

        }
    }




    @ApiOperation(value = "新增合成列表")
    @PostMapping("saveUploadFile")
    public ResponseEntity<Result> saveUser(@Valid @RequestBody UploadFileDto uploadFileDto) {
        String id = uploadFileDto.getId();
        //根据事件id查询总视频数据
        List<Map<String,Object>> list = uploadFileService.selectUploadFileByKey(id);
        if(!list.isEmpty()){
            uploadFileService.updateUploadFile(uploadFileDto);
        }else{
            //新增合成视频
            uploadFileService.insertUploadFile(uploadFileDto);
        }
        return ResultUtil.success(ResultCodeEnum.UPDATE_SUCCESS.getMessage());
    }

    @ApiOperation(value = "测试-清空队列数据")
    @GetMapping("clearQueue")
    public ResponseEntity<Result> clearQueue(@RequestParam String sessionId) {
        boolean flag = false;
        flag = WebSocketServer.concurrentHashMap.isEmpty();
        log.info("清空队列，当前队列是否为空:{}",flag);
        if(!flag){
            WebSocketServer.concurrentHashMap.get(sessionId).clear();
            log.info("当前队列长度:{}",WebSocketServer.concurrentHashMap.get(sessionId).size());
        }
        return ResultUtil.success(ResultCodeEnum.UPDATE_SUCCESS.getMessage());
    }


    @ApiOperation(value = "删除队列指定数据")
    @PostMapping("/removeQueue")
    @ResponseBody
    public ResponseEntity<Result> removeQueue(@RequestBody JSONObject param) {
        try {
            String key = param.getString("sessionId");
            String targetIds = param.getString("targetIds");
            if(!StringUtils.isEmpty(targetIds)){
                String[] str = Convert.toStrArray(targetIds);
                if(!QueueUtil.isEmpty()) {
                    Arrays.stream(str).forEach(x -> WebSocketServer.concurrentHashMap.get(key).remove(x));
                }
            }
            return ResultUtil.success(ResultCodeEnum.DELETE_SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("删除错误");
        }
    }



    /**
     * 	应用token服务接口
     */
    @PostMapping("/getAppToken")
    @ResponseBody
    public ResponseEntity<Result> getAppToken(@RequestBody Map<String, Object> queryMap) {
        String url = "http://ip:port/RZIMS-api/video/v1/getAppToken";
        Map<String, String> header = new HashMap<>();
        String result = okHttpUtil.postSend(url, header, queryMap);
        return ResultUtil.success(result);
    }

    /**
     * 	1.8	视频处理完成回调接口
     */
    @PostMapping("/setVideoFile")
    @ResponseBody
    public ResponseEntity<Result> setVideoFile(@RequestBody Map<String, Object> queryMap) {
        String url = "http://ip:port/RZIMS-api/video/v1/setVideoFile";
        Map<String, String> header = new HashMap<>();
        String result = okHttpUtil.postSend(url, header, queryMap);
        return ResultUtil.success(result);
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        try {
            if (null == fileName) {
                log.info("删除的单个文件不存在== " + fileName);
                return false;
            }
            File file = new File(fileName);
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    log.info("删除单个文件" + fileName + "成功！");
                    return true;
                } else {
                    log.info("删除单个文件" + fileName + "失败！");
                    return false;
                }
            } else {
                log.info("删除单个文件失败：" + fileName + "不存在！");
                return false;
            }
        } catch (Exception e) {
            log.error("删除单个文件失败== " + fileName, e);
        }
        return false;
    }


}
