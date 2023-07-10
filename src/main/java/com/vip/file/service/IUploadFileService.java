package com.vip.file.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.vip.file.common.Page;
import com.vip.file.model.dto.UploadFileDto;
import com.vip.file.model.entity.Files;
import com.vip.file.model.entity.UploadFileDo;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2022-06-27
 */
public interface IUploadFileService {

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public UploadFileDo selectUploadFileById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param uploadFile 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<UploadFileDo> selectUploadFileList(UploadFileDo uploadFile);

    /**
     * 新增总视频数据
     * 
     * @param uploadFileDto
     * @return 结果
     */
    int insertUploadFile(UploadFileDto uploadFileDto);

    /**
     * 修改总视频数据
     * 
     * @param uploadFileDto
     * @return 结果
     */
    public int updateUploadFile(UploadFileDto uploadFileDto);

    /**
     * 更新总视频合成视频数据
     *
     * @param uploadFileDo
     * @return 结果
     */
    int updateUploadFile(UploadFileDo uploadFileDo);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUploadFileByIds(String ids);

    /**
     * 删除总视频
     * @param id
     * @return
     */
    int deleteUploadFileById(String id) throws Exception;


    /**
     * 删除分视频
     * @param id
     * @return
     */
    int deleteFileById(String id) throws Exception;

    List<Map<String, Object>> selectUploadFilePage(Page page);

    /**
     * 根据事件id查询总视频数据
     * @param id
     * @return
     */
    List<Map<String, Object>> selectUploadFileByKey(String id);

    /**
     * 根据时间范围查询分视频
     * @param time
     * @return
     */
    List<Files> selectFilesByTime(String time,String id);

    /**
     * 根据targetId 查询所有file
     * @param id
     * @return
     */
    List<Files> selectFilesById(String id);

    /**
     * 根据合成视频id查询所有分视频列表
     * @param id
     * @return
     */
    List<Map<String, Object>> selectvideoFilePage(String id);

    List<Map<String, Object>> selectFileById(String id);

    List<Map<String, Object>> deleteUploadFileId(JSONObject param);

    List<Map<String, Object>> selectvideoFile(String id);

    /**
     * 根据targetId查询最早一条分视频
     * @param targetId
     * @return
     */
    Map<String,Object> getFileByTargetIdAndTime(String targetId);

}
