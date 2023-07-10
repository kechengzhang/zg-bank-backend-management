package com.vip.file.mapper;

import com.vip.file.model.query.SyntheticVideoQuery;
import com.vip.file.model.vo.SyntheticVideoFailureVO;
import com.vip.file.model.vo.SyntheticVideoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zkc
 * @description
 * @Date 2023/5/26 9:43 星期五
 * @Version 1.0
 */
@Mapper
public interface SyntheticVideoMapper {
    /**
     * 合成视频列表
     *
     * @param syntheticVideoQuery
     * @return
     */
    List<SyntheticVideoVO> querySyntheticVideo(@Param("syntheticVideoQuery") SyntheticVideoQuery syntheticVideoQuery);
    /**
     * 合成视频删除
     * @param ids
     * @return
     */
    int deleteSyntheticVideo(List<String> ids);

    /**
     * 失败视频查询
     * @param time
     * @param beginTime
     * @param endTime
     * @return
     */
    List<SyntheticVideoFailureVO> querySyntheticVideoFailure(@Param("time") String time,@Param("beginTime") String beginTime, @Param("endTime") String endTime);
}
