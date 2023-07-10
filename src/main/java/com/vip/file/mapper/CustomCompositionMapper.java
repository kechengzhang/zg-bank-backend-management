package com.vip.file.mapper;

import com.vip.file.model.vo.BankBranchVO;
import com.vip.file.model.vo.CustomCompositionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/23 14:06 星期二
 * @Version 1.0
 */
@Mapper
public interface CustomCompositionMapper {
    /**
     * 修改线程并发数
     *
     * @param corePool
     * @param synthesisTime
     * @param cronTime
     * @return
     */
    int updateCorePool(@Param("corePool") Integer corePool,@Param("synthesisTime")String synthesisTime,@Param("cronTime")String cronTime);

    /**
     *获取合成时间
     *
     * @return
     */
    CustomCompositionVO queryCustomComposition();

    /**
     *
     * 保存线程数合成时间
     * @param corePoll 线程数
     * @param cronTime cron表达式
     * @param synthesisTime
     * @return
     */
    int saveCorePoolCronTime(@Param("corePoll") Integer corePoll,@Param("synthesisTime")  String synthesisTime ,@Param("cronTime")String cronTime);

    /**
     * 获取合成视频(蔡工使用)
     *
     * @return
     */
    List<String> queryVideoCompose();

    /**
     * 查询银行信息
     * @param bankBranchName  银行名称
     * @return
     */
    List<BankBranchVO> queryBankInformation(@Param("bankBranchName") String bankBranchName);

    /**
     * 修改合成视频是否拉取
     *
     * @param list
     * @return
     */
    int updateVideoCompose(@Param("list") List<String> list);
}
