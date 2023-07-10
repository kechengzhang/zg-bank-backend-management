package com.vip.file.service;

import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.vip.file.model.dto.AddFileDto;
import com.vip.file.model.dto.GetFileDto;
import com.vip.file.model.entity.FileUploadFailedDO;
import com.vip.file.model.entity.Files;
import com.vip.file.model.entity.UploadFileParams;
import com.vip.file.model.response.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件上传下载 服务类
 * </p>
 *
 * @author LEON
 * @since 2020-05-29
 */
public interface IFileService {
    /**
     * 小文件上传
     *
     * @param file
     * @return
     */
    Result<String> uploadFiles(MultipartFile file);

    /**
     * 获取文件输入流
     *
     * @param id
     * @return
     */
    InputStream getFileInputStream(String id);

    /**
     * 获取指定文件详情
     *
     * @param id
     * @return
     */
    Result<Files> getFileDetails(String id);

    /**
     * 分页获取文件信息
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    Result<List<GetFileDto>> getFileList(Integer pageNo, Integer pageSize);

    /**
     * 检查文件MD5
     *
     * @param md5
     * @param fileName
     * @return
     */
    Result<Object> checkFileMd5(String md5, String fileName,String targetId);

    /**
     * 断点续传
     *
     * @param param
     * @param request
     * @return
     */
//    Result<Object> breakpointResumeUpload(UploadFileParams param, HttpServletRequest request);
    com.vip.file.bean.Result breakpointResumeUpload(UploadFileParams param, HttpServletRequest request);

    /**
     * 添加文件
     *
     * @param dto
     * @return
     */
    Result<String> addFile(AddFileDto dto);

//    /**
//     * 分块上传文件
//     * @param name
//     * @param md5
//     * @param size
//     * @param chunks
//     * @param chunk
//     * @param file
//     */
//    void uploadWithBlock(Long targetId,String name, String md5, Long size, Integer chunks, Integer chunk, MultipartFile file) throws IOException ;
//
//    /**
//     * 上传文件
//     * @param md5
//     * @param file
//     */
//    void upload(Long targetId,String name, String md5, MultipartFile file) throws IOException;

    /**
     * 检查Md5判断文件是否已上传
     * @param md5
     * @return
     */
    boolean checkMd5(String md5);

    /**
     * 用于判断是否有文件重复上传不同事件
     * @param fileName
     * @param targetId
     * @return
     */
    List<Files> getFilesByCons(String fileName,String targetId);

    /**
     * 上传失败视频保存
     *
     * @param fileUploadFailedDO
     * @return
     */
    int saveFileUploadFailed(FileUploadFailedDO fileUploadFailedDO);

    /**
     * 获取未上传文件总数
     * @return
     */
    int queryFileUploadFailed();
    /**
     * 获取上传文件路径
     *
     * @param targetId
     * @return
     */

    List<Map<String,String>> queryFilePath(String targetId);

    /**
     * 修改分视频文件路径
     *
     * @param id
     * @param filePath
     * @return
     */
    int updateTbFilesFilePath(String id,String filePath );

}
