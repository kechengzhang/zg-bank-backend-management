package com.vip.file.service;

import com.vip.file.bean.PageData;
import com.vip.file.model.dto.FileUploadFailedLogDTO;
import com.vip.file.model.entity.FileLogDO;
import com.vip.file.model.query.FileVideoLogQuery;
import com.vip.file.model.vo.FileUploadFailedVO;
import com.vip.file.model.vo.FileVideoLogRuleDetailsVO;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zkc
 *
 */
public interface FileVideoLogService {
    /**
     * 保存日志
     * @param fileLogDO
     * @return
     */
    int saveFileVideoLog(FileLogDO fileLogDO);

    /**
     *用户操作记录查询
     *
     * @param fileVideoLogQuery
     * @return
     */
    PageData queryUserActionRecord(FileVideoLogQuery fileVideoLogQuery);

    /**
     * 日志
     *
     * @param id
     * @return
     */
    FileVideoLogRuleDetailsVO queryUserActionRecordDetails(Long id);
/**
 * 上传失败文件查询
 */
    List<FileUploadFailedVO> queryUploadFailedFile();

    /**
     * 修改未上传文件
     *
     * @return
     */
    int updateUploadFailedFile();

    /**
     * 上传文件失败日志保存
     * @param fileUploadFailedLogDTO
     * @return
     */
    int saveUploadFailedLog( FileUploadFailedLogDTO fileUploadFailedLogDTO, HttpServletRequest httpServletRequest);
}
