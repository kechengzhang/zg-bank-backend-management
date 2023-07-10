package com.vip.file.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vip.file.bean.PageData;
import com.vip.file.mapper.FileVerificationRulesMapper;
import com.vip.file.model.dto.NewFileVerificationRulesDTO;
import com.vip.file.model.query.FileVerificationRulesQuery;
import com.vip.file.model.vo.FileVerificationRulesVO;
import com.vip.file.service.FileVerificationRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 13:38 星期五
 * @Version 1.0
 */
@Service
public class FileVerificationRulesServiceImpl implements FileVerificationRulesService {
    @Autowired
    private FileVerificationRulesMapper fileVerificationRulesMapper;
    @Override
    public int saveFileVerificationRules(NewFileVerificationRulesDTO fileVerificationRulesDTO) {
        return fileVerificationRulesMapper.saveFileVerificationRules(fileVerificationRulesDTO);
    }

    @Override
    public int updateFileVerificationRules(NewFileVerificationRulesDTO fileVerificationRulesDTO) {
        return fileVerificationRulesMapper.updateFileVerificationRules(fileVerificationRulesDTO);
    }

    @Override
    public PageData queryFileVerificationRules(FileVerificationRulesQuery fileVerificationRulesQuery) {
        PageHelper.startPage(fileVerificationRulesQuery.getPageNumber(), fileVerificationRulesQuery.getPageSize());
        PageData pageData = new PageData(new PageInfo(fileVerificationRulesMapper.queryFileVerificationRules(fileVerificationRulesQuery)));
        return pageData;
    }

    @Override
    public int deleteFileVerificationRules(List<Long> ids) {
        return fileVerificationRulesMapper.deleteFileVerificationRules(ids);
    }

    @Override
    public List<FileVerificationRulesVO> queryAllFileVerificationRules() {
        FileVerificationRulesQuery fileVerificationRulesQuery = new FileVerificationRulesQuery();
        fileVerificationRulesQuery.setUse(1);
        return fileVerificationRulesMapper.queryFileVerificationRules(fileVerificationRulesQuery);
    }

    @Override
    public String queryVerificationName(String name) {
        return fileVerificationRulesMapper.queryVerificationName(name);
    }
}
