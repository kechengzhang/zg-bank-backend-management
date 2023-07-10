package com.vip.file.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基础实体类：用于自动生成数据库表实体的公共字段
 *
 * @author wgb
 * @date 2020/3/26 11:47
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseEntity implements Serializable {

    private String id;
    /**
     * 创建时间，插入数据时自动填充
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createdTime;
    /**
     * 修改时间，插入、更新数据时自动填充
     */
    @TableField(value = "modified_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime modifiedTime;
//    /**
//     * 删除状态：插入数据时自动填充
//     */
//    @TableField(value = "delete_status", fill = FieldFill.INSERT)
//    @TableLogic
//    private boolean deleteStatus;

}
