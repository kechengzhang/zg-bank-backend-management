package com.vip.file.service.impl;

import com.vip.file.mapper.FileCleanMapper;
import com.vip.file.model.vo.FileCleanVO;
import com.vip.file.model.vo.FileTargetIdSizeVO;
import com.vip.file.service.FileCleanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/7/3 13:34 星期一
 * @Version 1.0
 */
@Service
@Slf4j
public class FileCleanServiceImpl implements FileCleanService {
    @Autowired
    private FileCleanMapper fileCleanMapper;
    @Override
    public FileCleanVO getFileClean() {
        FileCleanVO fileCleanVO = null;
        try {
            fileCleanVO = fileCleanMapper.getFileClean();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return fileCleanVO;
    }

    @Override
    public List<FileTargetIdSizeVO> getFileTargetIdSize() {
        return fileCleanMapper.getFileTargetIdSize();
    }
}
