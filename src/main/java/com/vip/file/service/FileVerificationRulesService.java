package com.vip.file.service;

import com.vip.file.bean.PageData;
import com.vip.file.model.dto.NewFileVerificationRulesDTO;
import com.vip.file.model.query.FileVerificationRulesQuery;
import com.vip.file.model.vo.FileVerificationRulesVO;

import java.util.List;

/**
 * @author  zkc
 *
 */
public interface FileVerificationRulesService {
    /**
     * 文件校验规则新增
     *
     * @param fileVerificationRulesDTO
     * @return
     */
    int saveFileVerificationRules(NewFileVerificationRulesDTO fileVerificationRulesDTO);
    /**
     * 文件校验规则修改
     * @param fileVerificationRulesDTO
     * @return
     */
    int updateFileVerificationRules(NewFileVerificationRulesDTO fileVerificationRulesDTO);
    /**
     * 文件校验规则查询
     * @param fileVerificationRulesQuery
     * @return
     */
    PageData queryFileVerificationRules(FileVerificationRulesQuery fileVerificationRulesQuery);
    /**
     * 文件校验规则删除
     * @param ids
     * @return
     */
    int deleteFileVerificationRules(List<Long> ids);

    /**
     * 文件名校验规则查询
     * @return
     */
    List<FileVerificationRulesVO> queryAllFileVerificationRules();

    /**
     * 查询规则名称是否重复
     *
     * @param name
     * @return
     */
    String queryVerificationName(String name);

}
