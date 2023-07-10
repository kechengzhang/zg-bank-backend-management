package com.vip.file.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vip.file.bean.PageData;
import com.vip.file.mapper.CustomCompositionMapper;
import com.vip.file.mapper.SyntheticVideoMapper;
import com.vip.file.model.dto.SyntheticVideoDTO;
import com.vip.file.model.query.SyntheticVideoFailureQuery;
import com.vip.file.model.query.SyntheticVideoQuery;
import com.vip.file.model.vo.BankBranchVO;
import com.vip.file.model.vo.CustomCompositionVO;
import com.vip.file.model.vo.SyntheticVideoFailureVO;
import com.vip.file.service.IUploadFileService;
import com.vip.file.service.SyntheticVideoService;
import com.vip.file.utils.DateUtils;
import com.vip.file.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 9:40 星期五
 * @Version 1.0
 */
@Service
@Slf4j
public class SyntheticVideoServiceImpl implements SyntheticVideoService {
    @Value("${file.compose-path}")
    private String composePath;
    @Autowired
    private SyntheticVideoMapper syntheticVideoMapper;
    @Autowired
    private CustomCompositionMapper customCompositionMapper;
    @Autowired
    private IUploadFileService uploadFileService;

    @Override
    public PageData querySyntheticVideo(SyntheticVideoQuery syntheticVideoQuery) {
        PageHelper.startPage(syntheticVideoQuery.getPageNumber(), syntheticVideoQuery.getPageSize());
        PageData pageData = new PageData(new PageInfo(syntheticVideoMapper.querySyntheticVideo(syntheticVideoQuery)));
        return pageData;
    }

    @Override
    public int deleteSyntheticVideo(SyntheticVideoDTO syntheticVideoDTO) {
        try {
            syntheticVideoDTO.getName().stream().forEach(name -> {
                //删除服务器合成视频
                FileUtils.deleteFile(name,composePath);
            });
            //删除上传视频
            syntheticVideoDTO.getId().stream().forEach(id -> {
                try {
                    uploadFileService.deleteUploadFileById(id);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return syntheticVideoMapper.deleteSyntheticVideo(syntheticVideoDTO.getId());
    }

    @Override
    public PageData querySyntheticVideoFailure(SyntheticVideoFailureQuery syntheticVideoFailureQuery) {
        PageHelper.startPage(syntheticVideoFailureQuery.getPageNumber(), syntheticVideoFailureQuery.getPageSize());
        //获取合成时间
        CustomCompositionVO customCompositionVO = customCompositionMapper.queryCustomComposition();
        String time = DateUtils.getDate() + " " + customCompositionVO.getSynthesisTime();
        PageData pageData = new PageData(new PageInfo(syntheticVideoMapper.querySyntheticVideoFailure(time, syntheticVideoFailureQuery.getBeginTime(), syntheticVideoFailureQuery.getEndTime())));
        return pageData;
    }

    @Override
    public List<String> queryVideoCompose() {
        List<String> list = customCompositionMapper.queryVideoCompose();
        if (list.size() > 0) {
            customCompositionMapper.updateVideoCompose(list);
        }
        return list;
    }

    @Override
    public List<BankBranchVO> queryBankInformation(String bankBranchName) {
        return customCompositionMapper.queryBankInformation(bankBranchName);
    }
}
