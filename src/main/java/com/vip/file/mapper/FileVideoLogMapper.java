package com.vip.file.mapper;

import com.alibaba.fastjson.JSONArray;
import com.vip.file.model.entity.FileLogDO;
import com.vip.file.model.entity.FileVideoLogRuleDetailsDO;
import com.vip.file.model.query.FileVideoLogQuery;
import com.vip.file.model.vo.FileUploadFailedVO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;
import com.vip.file.model.vo.FileVideoLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zkc
 *
 */
@Mapper
public interface FileVideoLogMapper {
    /**
     * 保存日志
     * @param fileLogDO
     * @return
     */
    int saveFileVideoLog(FileLogDO fileLogDO);

    /**
     * 用户操作记录查询
     *
     * @param fileVideoLogQuery
     * @return
     */
    List<FileVideoLogVO> queryUserActionRecord(@Param("fileVideoLogQuery") FileVideoLogQuery fileVideoLogQuery);

    /**
     * 日志详情查询
     * @param id
     * @return
     */
    FileVideoLogRuleDetailsDO queryUserActionRecordDetails(@Param("id") Long id);

    /**
     * 通过规则id查询规则名称
     *
     * @param ids
     * @return
     */
    List<String>queryVerificationName(@Param("ids") JSONArray ids);

    /**
     *
     * 上传失败文件查询
     * @return
     */
    List<FileUploadFailedVO> queryUploadFailedFile();

    /**
     * 修改未上传文件
     *
     * @return
     */
    int updateUploadFailedFile();

    /**
     * 获取分视频名称
     *
     * @param id 分视频id
     * @return
     */
    String queryFileName(@Param("id") String id);
}
