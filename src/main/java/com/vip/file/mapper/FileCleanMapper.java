package com.vip.file.mapper;

import com.vip.file.model.vo.FileCleanVO;
import com.vip.file.model.vo.FileTargetIdSizeVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/7/3 14:15 星期一
 * @Version 1.0
 */
@Repository
public interface FileCleanMapper {
    /**
     * 获取文件磁盘占比
     *
     * @return
     */
    FileCleanVO getFileClean();

    /**
     * 获取上传文件大小
     * @return
     */
    List<FileTargetIdSizeVO> getFileTargetIdSize();
}
