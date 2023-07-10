package com.vip.file.mapper;

import com.vip.file.model.dto.NewFileVerificationRulesDTO;
import com.vip.file.model.query.FileVerificationRulesQuery;
import com.vip.file.model.vo.FileVerificationRulesVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zkc
 *
 */
@Mapper
public interface FileVerificationRulesMapper {
    /**
     * 文件校验规则新增
     *
     * @param fileVerificationRulesDTO
     * @return
     */
    int saveFileVerificationRules(@Param("fileVerificationRulesDTO") NewFileVerificationRulesDTO fileVerificationRulesDTO);


    /**
     * 文件校验规则修改
     *
     * @param fileVerificationRulesDTO
     * @return
     */
    int updateFileVerificationRules(@Param("fileVerificationRulesDTO") NewFileVerificationRulesDTO fileVerificationRulesDTO);

    /**
     * 文件校验规则查询
     *
     * @param fileVerificationRulesQuery
     * @return
     */
    List<FileVerificationRulesVO> queryFileVerificationRules(@Param("fileVerificationRulesQuery") FileVerificationRulesQuery fileVerificationRulesQuery);

    /**
     * 文件校验规则删除
     *
     * @param ids
     * @return
     */
    int deleteFileVerificationRules(@Param("ids")List<Long> ids);

    /**
     * 查询规则名称是否重复
     *
     * @param name
     * @return
     */
    String queryVerificationName(String name);
}
