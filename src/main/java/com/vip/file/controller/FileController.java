package com.vip.file.controller;

import com.vip.file.aspect.LogTrack;
import com.vip.file.bean.ResultCodeEnum;
import com.vip.file.config.SystemException;
import com.vip.file.constant.SysConstant;
import com.vip.file.constant.UrlConstant;
import com.vip.file.model.dto.AddFileDto;
import com.vip.file.model.dto.GetFileDto;
import com.vip.file.model.entity.Files;
import com.vip.file.model.entity.UploadFileParams;
import com.vip.file.model.response.ErrorCode;
import com.vip.file.model.response.RestResponse;
import com.vip.file.model.response.RestResponses;
import com.vip.file.model.response.Result;
import com.vip.file.quartz.CustomCompositionSchedulingTask;
import com.vip.file.service.IFileService;
import com.vip.file.service.IUploadFileService;
import com.vip.file.utils.DateUtils;
import com.vip.file.utils.EmptyUtils;
import com.vip.file.utils.EncodingUtils;
import com.vip.file.utils.InputStreamUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件上传下载 前端控制器
 * </p>
 *
 * @author LEON
 * @since 2020-05-29
 */
@Slf4j
@Api(tags = "文件上传下载 前端控制器")
@RestController
@RequestMapping(UrlConstant.API + "/file")
@RequiredArgsConstructor
public class FileController {
    @Value("${file.save-path:/data-center/files/vip-file-manager}")
    private String savePath;
    private final IFileService fileService;
    @Autowired
    private IUploadFileService iUploadFileService;
    @Value("${compose.compose_time}")
    private Integer compose_time;
    @Value("${compose.compose_rules}")
    private String compose_rules;

    /**
     * 文件列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "文件列表")
    @GetMapping(value = "/list")
    public RestResponse<List<GetFileDto>> getFileList(@RequestParam Integer pageNo, @RequestParam Integer pageSize) throws IOException {
        return RestResponses.newResponseFromResult(fileService.getFileList(pageNo, pageSize));
    }

    @ApiOperation(value = "普通上传方式上传文件：用于小文件的上传，等待时间短，不会产生配置数据")
    @PostMapping("/upload")
    public RestResponse<String> uploadFiles(MultipartFile file) {
        if (file.isEmpty()) {
            return RestResponses.newFailResponse(ErrorCode.INVALID_PARAMETER, "文件不能为空");
        }
        return RestResponses.newResponseFromResult(fileService.uploadFiles(file));
    }

    /**
     * 添加文件
     * 断点续传完成后上传文件信息进行入库操作
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加文件,断点续传完成后上传文件信息进行入库操作")
    @PostMapping("/adds")
    public RestResponse<String> addFile(@RequestBody AddFileDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return RestResponses.newFailResponse(ErrorCode.INVALID_PARAMETER, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return RestResponses.newResponseFromResult(fileService.addFile(dto));
    }

    /**
     * 检查文件MD5（文件MD5若已存在进行秒传）
     *
     * @param md5
     * @param fileName
     * @return
     */
    @ApiOperation(value = "检查文件MD5（文件MD5若已存在进行秒传）")
    @GetMapping(value = "/checkFile")
    public RestResponse<Object> checkFileMd5(String md5, String fileName,String targetId) {
        List<Files> list = fileService.getFilesByCons(fileName,targetId);
        if(!list.isEmpty()){
            return RestResponses.newFailResponse(ErrorCode.DATA_EXIST,"文件已存在");
        }
        return RestResponses.newResponseFromResult(fileService.checkFileMd5(md5, fileName,targetId));
    }


    /**
     * 断点续传方式上传文件：用于大文件上传(二期使用)
     *
     * @param param
     * @param request
     * @return
     */
    @ApiOperation(value = "断点续传方式上传文件：用于大文件上传")
    @PostMapping(value = "/breakPointUpload", consumes = "multipart/*", headers = "content-type=multipart/form-data", produces = "application/json;charset=UTF-8")
    @LogTrack("前端系统,上传分视频,添加分视频文件,上传")
    public com.vip.file.bean.Result breakpointResumeUpload(UploadFileParams param, HttpServletRequest request) {
        String currentTime = DateUtils.getTime();
        if(StringUtils.isEmpty(param.getUid())){
            return com.vip.file.bean.Result.failure(ResultCodeEnum.ERROR,ResultCodeEnum.PARAMETER_ERROR.getMessage());
        }
        int fileCount = param.getFileCount();
        int chunk = param.getChunk();
        int fileLength = param.getFileLength();
        String targetId = param.getTargetId();
        //获取已经上传完成的第一条数据
        Map<String,Object> fileMap = iUploadFileService.getFileByTargetIdAndTime(targetId);
        boolean flag = false;
        if(fileMap!=null){
            String compareTime = DateUtils.addDateMinut(compose_rules,fileMap.get("created_time").toString(),compose_time);
            flag = DateUtils.compareTime(currentTime,compareTime);
        }
        if(fileCount-fileLength == 1&&chunk==0&&flag){
            return com.vip.file.bean.Result.failure(ResultCodeEnum.ERROR,ResultCodeEnum.COMPOSE_ERROR.getMessage());
        }
        CustomCompositionSchedulingTask.uploadFileMap.put(param.getTargetId(),false);
        return fileService.breakpointResumeUpload(param, request);
    }

    @ApiOperation(value = "秒传")
    @GetMapping("/quickUpload")
    public boolean upload(String md5) {
        return fileService.checkMd5(md5);
    }

    /**
     * 图片/PDF查看
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "图片/PDF查看")
    @GetMapping(value = "/view/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> viewFilesImage(@PathVariable String id) throws IOException {
        Result<Files> fileDetails = fileService.getFileDetails(id);
        if (fileDetails.isSuccess()) {
            if (!EmptyUtils.basicIsEmpty(fileDetails.getData().getType()) && !SysConstant.IMAGE_TYPE.contains(fileDetails.getData().getType().substring(fileDetails.getData().getType().indexOf("/")))) {
                throw new SystemException(ErrorCode.FILE_ERROR.getCode(), "非图片/PDF类型请先下载");
            }
        } else {
            throw new SystemException(fileDetails.getErrorCode().getCode(), fileDetails.getDescription());
        }
        InputStream inputStream = fileService.getFileInputStream(id);
        return new ResponseEntity<>(InputStreamUtils.inputStreamToByte(inputStream), HttpStatus.OK);
    }

    /**
     * 文件下载
     *
     * @param id
     * @param isSource
     * @param request
     * @param response
     */
    @ApiOperation(value = "文件下载")
    @GetMapping(value = "/download/{id}")
    public void viewFilesImage(@PathVariable String id, @RequestParam(required = false) Boolean isSource, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            Result<Files> fileDetails = fileService.getFileDetails(id);
            if (!fileDetails.isSuccess()) {
                throw new SystemException(fileDetails.getErrorCode().getCode(), fileDetails.getDescription());
            }
            String filename = (!EmptyUtils.basicIsEmpty(isSource) && isSource) ? fileDetails.getData().getFileName() : fileDetails.getData().getFilePath();
            inputStream = fileService.getFileInputStream(id);
            response.setHeader("Content-Disposition", "attachment;filename=" + EncodingUtils.convertToFileName(request, filename));
            // 获取输出流
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("文件下载出错", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

