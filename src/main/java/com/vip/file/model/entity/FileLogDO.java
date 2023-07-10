package com.vip.file.model.entity;

import lombok.Data;

/**
 * @author zkc
 * @description
 * @Date 2023/5/29 15:04 星期一
 * @Version 1.0
 */
@Data
public class FileLogDO {
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户id
     *
     */
    private Long userId;
    /**
     * 业务模块
     *
     */
    private String businessModule;
    private String operationObjects;//'操作对象',
    private String createTime; //'操作时间',
    private String operationResult;//'操作结果',
    private String requestParameter; //'请求参数',
    private String operationType; //'操作类型',
    /**
     * 请求ip
     *
     */
    private Long ip;
    /**
     * 失败原因
     *
     */
    private String failureReason;
}

