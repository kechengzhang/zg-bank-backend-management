package com.vip.file.model.vo;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/30 9:20 星期二
 * @Version 1.0
 */
@Data
public class FileVideoLogVO {
    private Long id;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 业务模块
     *
     */
    private String businessModule;
    /**
     * 操作对象
     */
    private String operationObjects;
    /**
     * 操作状态
     *
     */
    private String operationResult;
    /**
     *  操作类型
     *
     */
    private String operationType;
    /**
     *  操作时间
     *
     */
    private String operationTime;
}
