package com.vip.file.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zw
 * @date 2022/8/12 17:50
 */
@Data
@Accessors(chain = true)
@TableName("file_lock")
@ApiModel(value="Files对象", description="")
public class FileLock {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "事件id")
    @TableField(value = "targetId")
    private String targetId;

    @ApiModelProperty(value = "是否正在合成或已经合成成功 0否1是")
    @TableField(value = "isCompose")
    private String isCompose;
}
