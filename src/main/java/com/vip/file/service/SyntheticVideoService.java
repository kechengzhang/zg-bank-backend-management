package com.vip.file.service;

import com.vip.file.bean.PageData;
import com.vip.file.model.dto.SyntheticVideoDTO;
import com.vip.file.model.query.SyntheticVideoFailureQuery;
import com.vip.file.model.query.SyntheticVideoQuery;
import com.vip.file.model.vo.BankBranchVO;
import com.vip.file.model.vo.SyntheticVideoFailureVO;

import java.util.List;

/**
 * @author  zkc
 *
 */
public interface SyntheticVideoService {
    /**
     * 合成视频查询
     *
     * @param syntheticVideoQuery
     * @return
     */
    PageData querySyntheticVideo(SyntheticVideoQuery syntheticVideoQuery);

    /**
     * 合成视频删除
     *
     * @param syntheticVideoDTO
     * @return
     */
    int deleteSyntheticVideo(SyntheticVideoDTO syntheticVideoDTO);

    /**
     * 合成失败视频
     * @param syntheticVideoFailureQuery
     * @return
     */
    PageData querySyntheticVideoFailure(SyntheticVideoFailureQuery syntheticVideoFailureQuery);

    /**
     * 合成视频查询
     *
     * @return
     */
    List<String> queryVideoCompose();

    /**
     * 银行信息查询
     * @param bankBranchName  银行名称
     * @return
     */
    List<BankBranchVO> queryBankInformation(String bankBranchName);
}
