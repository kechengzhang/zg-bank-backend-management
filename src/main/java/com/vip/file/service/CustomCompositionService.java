package com.vip.file.service;

import com.vip.file.model.vo.CustomCompositionVO;

/**
 *
 * @author zkc
 *
 */
public interface CustomCompositionService {
    /**
     * 修改并发线程数
     *
     * @param corePool
     * @param cronTime
     * @return
     */
    int updateCorePool(Integer corePool,String synthesisTime,String cronTime);

    /**
     *
     * @return
     */
    CustomCompositionVO queryCustomComposition();
    /**
     * 保存合成时间、合成并发数
     * @param cronTime
     * @param corePoll
     * @param synthesisTime
     * @return
     */
    int saveCorePoolCronTime(Integer corePoll,String synthesisTime ,String cronTime);

    String getCronTime();
}
