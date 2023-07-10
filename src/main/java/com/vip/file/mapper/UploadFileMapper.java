package com.vip.file.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vip.file.model.entity.Files;
import com.vip.file.model.entity.UploadFileDo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2022-06-27
 */
@Mapper
public interface UploadFileMapper extends BaseMapper<UploadFileDo> {
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
     UploadFileDo selectUploadFileById(String id);

    /**
     * 查询总视频列表
     * 
     * @param uploadFile 查询条件
     * @return 【请填写功能名称】集合
     */
     List<UploadFileDo> selectUploadFileList(UploadFileDo uploadFile);

//    /**
//     * 新增总视频数据
//     *
//     * @param uploadFileDto
//     * @return
//     */
//    public int insertUploadFile(UploadFileDto uploadFileDto);

    /**
     * 修改【请填写功能名称】
     * 
     * @param uploadFile 【请填写功能名称】
     * @return 结果
     */
    int updateUploadFile(UploadFileDo uploadFile);

    /**
     * 删除分视频
     * 
     * @param id
     * @return
     */
    int deleteUploadFileById(String id);

    /**
     * 根据时间范围查询分视频
     * @param time
     * @return
     */
    @Select({
            "<script>select * from tb_files where <![CDATA[created_time < #{time}]]> and target_id = #{id} and status = 1 </script> "
    })
    List<Files> selectFilesByTime(@Param("time") String time,@Param("id") String id);

    List<Files> selectFilesById(@Param("id") String id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     int deleteUploadFileByIds(String[] ids);

    /**
     * 根据targetId查询最早一条分视频
     * @param targetId
     * @return
     */
    @Select({
            "select tf.*,uf.isCompose from tb_files tf left join upload_file uf " +
                    " on tf.target_id = uf.id " +
                    "where tf.target_id = #{targetId} and tf.status = 1 order by tf.created_time asc limit 1 "
    })
    Map<String,Object> getFileByTargetIdAndTime(@Param("targetId") String targetId);

    @SelectProvider(type = UploadFileSqlProvider.class, method = "selectUploadFilePage")
    List<Map<String, Object>> selectUploadFilePage();

    @SelectProvider(type = UploadFileSqlProvider.class, method = "selectvideoFilePage")
    List<Map<String, Object>> selectvideoFilePage(String id);

    @SelectProvider(type = UploadFileSqlProvider.class, method = "selectFileById")
    List<Map<String, Object>> selectFileById(String id);

    @SelectProvider(type = UploadFileSqlProvider.class, method = "deleteUploadFileId")
    List<Map<String, Object>> deleteUploadFileId(JSONObject param);

    @SelectProvider(type = UploadFileSqlProvider.class, method = "selectvideoFile")
    List<Map<String, Object>> selectvideoFile(String id);

    /**
     * 逻辑删除总视频
     * @param targetId
     * @return
     */
    @UpdateProvider(type = UploadFileSqlProvider.class, method = "updateUploadFileById")
    int updateUploadFileById(Long targetId);

    @SelectProvider(type = UploadFileSqlProvider.class, method = "selectUploadFileByKey")
    List<Map<String, Object>> selectUploadFileByKey(String id);

}
