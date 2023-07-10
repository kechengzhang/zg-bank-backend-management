package com.vip.file.service.impl;

import cn.novelweb.tool.upload.local.LocalUpload;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.constant.SysConstant;
import com.vip.file.mapper.CustomCompositionMapper;
import com.vip.file.mapper.FilesMapper;
import com.vip.file.mapper.UploadFileMapper;
import com.vip.file.model.dto.AddFileDto;
import com.vip.file.model.dto.GetFileDto;
import com.vip.file.model.entity.FileUploadFailedDO;
import com.vip.file.model.entity.Files;
import com.vip.file.model.entity.UploadFileParams;
import com.vip.file.model.response.ErrorCode;
import com.vip.file.model.response.Result;
import com.vip.file.model.response.Results;
import com.vip.file.model.vo.CustomCompositionVO;
import com.vip.file.quartz.CustomCompositionSchedulingTask;
import com.vip.file.service.IFileService;
import com.vip.file.utils.*;
import com.vip.file.websocket.constant.ThirdInterfaceConstant;
import com.vip.file.websocket.service.MessagePushService;
import com.vip.file.websocket.service.WebSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * <p>
 * 文件上传下载 服务实现类
 * </p>
 *
 * @author LEON
 * @since 2020-05-29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {
    @Autowired
    private UploadFileMapper uploadFileMapper;
    private final FilesMapper filesMapper;
    @Value("${file.save-path:/data-center/files/vip-file-manager}")
    private String savePath;
    @Value("${file.conf-path:/data-center/files/vip-file-manager/conf}")
    private String confFilePath;
    @Autowired
    MessagePushService messagePushService;
    @Value("${compose.compose_time}")
    private Integer composeTime;

    @Value("${compose.compose_rules}")
    private String compose_rules;

    @Autowired
    private Cache<String, Object> cache;
    @Autowired
    private CustomCompositionMapper customCompositionMapper;
    @Override
    public Result<List<GetFileDto>> getFileList(Integer pageNo, Integer pageSize) {
        try {
            PageHelper.startPage(pageNo, pageSize);
            List<GetFileDto> result = filesMapper.selectFileList();
            PageInfo<GetFileDto> pageInfo = new PageInfo<>(result);
            return Results.newSuccessResult(pageInfo.getList(), "查询成功", pageInfo.getTotal());
        } catch (Exception e) {
            log.error("获取文件列表出错", e);
        }
        return Results.newFailResult(ErrorCode.DB_ERROR, "查询失败");
    }

    @Override
    public Result<String> uploadFiles(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            // 文件名非空校验
            if (EmptyUtils.basicIsEmpty(fileName)) {
                return Results.newFailResult(ErrorCode.FILE_ERROR, "文件名不能为空");
            }
            // 大文件判定
            if (file.getSize() > SysConstant.MAX_SIZE) {
                return Results.newFailResult(ErrorCode.FILE_ERROR, "文件过大，请使用大文件传输");
            }
            // 生成新文件名
            String suffixName = file.getContentType();
            String newName = UuidUtils.uuid() + suffixName;
            // 重命名文件
            File newFile = new File(savePath, newName);
            // 如果该存储路径不存在则新建存储路径
            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }
            // 文件写入
            file.transferTo(newFile);
            // 保存文件信息
            Files files = new Files().setFilePath(newName).setFileName(fileName).setType(suffixName == null ? null : suffixName.substring(1));
            filesMapper.insert(files);
            return Results.newSuccessResult(String.valueOf(files.getId()), "上传完成");
        } catch (Exception e) {
            log.error("上传协议文件出错", e);
        }
        return Results.newFailResult(ErrorCode.FILE_ERROR, "上传失败");
    }

    @Override
    public Result<Object> checkFileMd5(String md5, String fileName,String targetId) {
        try {
//            cn.novelweb.tool.http.Result result = LocalUpload.checkFileMd5(md5, fileName, confFilePath+"\\"+targetId, savePath+"\\"+targetId);
            cn.novelweb.tool.http.Result result = LocalUpload.checkFileMd5(md5, fileName, confFilePath+"/"+targetId, savePath+"/"+targetId);
            if("0".equals(result.getCode())){

            }
            return NovelWebUtils.forReturn2(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Results.newFailResult(ErrorCode.FILE_UPLOAD, "上传失败");
    }

    @Override
    public com.vip.file.bean.Result breakpointResumeUpload(UploadFileParams param, HttpServletRequest request) {
        try {
            String targetId = param.getTargetId();
            // 这里的 chunkSize(分片大小) 要与前端传过来的大小一致
            cn.novelweb.tool.http.Result result = LocalUpload.fragmentFileUploader(param,confFilePath+"/"+targetId, savePath+"/"+targetId, 5242880L, request);
            String code = result.getCode();
            String key = param.getSessionId();
            //将上传进度保存到数据库中
            Files file = new Files();
            if("200".equals(code)||"201".equals(code)){
                AddFileDto dto = getFileDto(param,targetId,code);
                String uid = dto.getId();
                BeanUtils.copyProperties(dto, file);
                file.setId(uid);
                Files tempFile = filesMapper.selectById(uid);
                if(tempFile!=null&&tempFile.getId()!=null){
                    filesMapper.updateById(file);
                }else{
                    filesMapper.insertFiles(file);
                }
            }
            //如果文件上传完成，查看队列中是否还有event，如果有，将event推送给前端，通知前端开始上传下一个文件
            if("201".equals(code)){
                //剩余文件数量
                int count = param.getFileLength();
                //文件总数
                int fileCount = param.getFileCount();
                if(fileCount-count == 1){
                    JSONObject jsonObject = new JSONObject();
//                    Map<String,Object> fileMap = uploadFileMapper.getFileByTargetIdAndTime(targetId);
//                    if(fileMap!=null&&fileMap.size()>0){
//                        String time = fileMap.get("created_time").toString();
//                        jsonObject.put("composeTime", DateUtils.addDateMinut(compose_rules,time,compose_time));
//                    }else{
//                        jsonObject.put("composeTime","");
//                    }
                    jsonObject.put("fileId",targetId);
//                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    jsonObject.put("currentTime",sf.format(new Date()));
                    //获取视频合成时间,前端获取当前时间
                    String endTime =WebSocketServer.getComposeTime();
                    String beginTime = DateUtils.reduceDateMinute(endTime,composeTime);
                    jsonObject.put("beginTime",DateUtils.getDate()+" "+beginTime);
                    jsonObject.put("endTime",DateUtils.getDate()+" "+endTime);
                    messagePushService.sendComposeTime(ThirdInterfaceConstant.WebSocketMsg.CURRENT_TIME.getName(), jsonObject);
                    log.info("第一个文件上传完毕，向前端发送合成时间:{}",jsonObject.toJSONString());
                }
                if(count == 0){
                   param.setUniqueIdentification(UUID.randomUUID().toString());
                   //最后一个文件传完，释放targetId,可以合成
                    CustomCompositionSchedulingTask.uploadFileMap.remove(targetId);
                    LinkedBlockingDeque<String> LINKED_BLOCKING_QEQUE = WebSocketServer.concurrentHashMap.get(key);
                    if(!LINKED_BLOCKING_QEQUE.isEmpty()){
                        String fileId = null;
                        String tempQueue = LINKED_BLOCKING_QEQUE.poll();
                        if(tempQueue.equals(targetId)){
                            fileId = LINKED_BLOCKING_QEQUE.poll();
                        }else{
                            fileId = tempQueue;
                        }
                        messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.EVENT_SUCCESS.getName(), fileId);
                    }else{
                        messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.EVENT_SUCCESS.getName(), null);
                    }
                }
            }
            return com.vip.file.bean.Result.success(ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            CustomCompositionSchedulingTask.uploadFileMap.remove(param.getTargetId());
            messagePushService.sendStatus(ThirdInterfaceConstant.WebSocketMsg.EVENT_FAILED.getName(), param.getTargetId());
            return com.vip.file.bean.Result.failure(ResultCodeEnum.ERROR,ResultCodeEnum.UPLOAD_ERROR.getMessage());
        }
    }

    @Override
    public Result<String> addFile(AddFileDto dto) {
        try {
            Files file = new Files();
            BeanUtils.copyProperties(dto, file);
//            dto.setFilePath(savePath+"\\"+dto.getFileName());
            dto.setFilePath(savePath+"/"+dto.getFileName());
            file.setTargetId(dto.getTargetId());
            if (filesMapper.fileIsExist(dto.getFileName())) {
                return Results.newSuccessResult(null, "添加成功");
            } else if (filesMapper.insert(file.setFilePath(dto.getFilePath())) == 1) {
                return Results.newSuccessResult(null, "添加成功");
            }
        } catch (Exception e) {
            log.error("添加文件出错", e);
        }
        return Results.newFailResult(ErrorCode.FILE_UPLOAD, "添加失败");
    }
    @Override
    public boolean checkMd5(String md5) {
        Files files = new Files();
        files.setMd5(md5);
        return filesMapper.getByFileByMd5(files) == null;
    }

    @Override
    public List<Files> getFilesByCons(String fileName, String targetId) {
        return filesMapper.getFilesByCons(fileName,targetId);
    }

    @Override
    public int saveFileUploadFailed(FileUploadFailedDO fileUploadFailedDO) {
        return filesMapper.saveFileUploadFailed(fileUploadFailedDO);
    }

    @Override
    public int queryFileUploadFailed() {
        return filesMapper.queryFileUploadFailed();
    }

    @Override
    public List<Map<String, String>> queryFilePath(String targetId) {
        return filesMapper.queryFilePath(targetId);
    }

    @Override
    public int updateTbFilesFilePath(String id, String filePath) {
        return filesMapper.updateTbFilesFilePath(id,filePath);
    }


    @Override
    public InputStream getFileInputStream(String id) {
        try {
            Files files = filesMapper.selectById(id);
//            File file = new File(savePath + File.separator + files.getFilePath());
            File file = new File(files.getFilePath());
            return new FileInputStream(file);
        } catch (Exception e) {
            log.error("获取文件输入流出错:{}", e);
        }
        return null;
    }

    @Override
    public Result<Files> getFileDetails(String id) {
        try {
            Files files = filesMapper.selectById(id);
            return Results.newSuccessResult(files, "查询成功");
        } catch (Exception e) {
            log.error("获取文件详情出错", e);
        }
        return Results.newFailResult(ErrorCode.DB_ERROR, "查询失败");
    }

       public  AddFileDto getFileDto(UploadFileParams param,String targetId,String code){
           String uid = param.getUid();
           String name = param.getName();
           int chunk = param.getChunk();
           int chunks = param.getChunks();
           Long size = param.getSize();
           String type = param.getType();
           AddFileDto dto = new AddFileDto();
           dto.setId(uid);
           dto.setFilePath(savePath+"/"+targetId+"/"+name);
           dto.setFileName(name);
           dto.setTargetId(param.getTargetId());
           dto.setMd5(param.getMd5());
           dto.setStatus(0);
           dto.setSize(size);
           dto.setChunk(chunk);
           dto.setChunks(chunks);
           dto.setType(type);
           dto.setBankBranchId(param.getBankBranchId());
           dto.setBankBranchName(param.getBankBranchName());
           dto.setType(type);
           if("201".equals(code)){
               dto.setStatus(1);
           }
           return dto;
       }


    public static void main(String[] args) {

        try {
            cn.novelweb.tool.http.Result result = LocalUpload.fragmentFileUploader(null, null, null, 5242880L, null);
            System.out.println(result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
