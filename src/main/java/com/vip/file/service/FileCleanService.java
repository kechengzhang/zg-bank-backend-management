package com.vip.file.service;

import com.vip.file.model.vo.FileCleanVO;
import com.vip.file.model.vo.FileTargetIdSizeVO;

import java.util.List;

/**
 * @author zkc
 *
 */
public interface FileCleanService {
    /**
     * 获取文件占比
     *
     * @return
     */
    FileCleanVO getFileClean();

    /**
     * 获取文件大小
     * @return
     */
    List<FileTargetIdSizeVO> getFileTargetIdSize();
}
