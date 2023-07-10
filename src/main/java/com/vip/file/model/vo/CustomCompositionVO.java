package com.vip.file.model.vo;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/23 14:19 星期二
 * @Version 1.0
 */
@Data
public class CustomCompositionVO {
    /**
     * 合成并发数
     */

    private Integer corePool;
    /**
     * 合成时间
     */

    private String synthesisTime;
    /**
     * 合成时间
     */

    private String cron;
}
