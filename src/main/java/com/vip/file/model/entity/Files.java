package com.vip.file.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author LEON
 * @since 2020-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_files")
@ApiModel(value="Files对象", description="")
public class Files extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "目标对象ID")
    private String targetId;

    @ApiModelProperty(value = "文件位置")
    private String filePath;

    @ApiModelProperty(value = "原始文件名")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "md5")
    private String md5;

    @ApiModelProperty(value = "chunk")
    private Integer chunk;

    @ApiModelProperty(value = "chunks")
    private Integer chunks;

    @ApiModelProperty(value = "size")
    private Long size;

    @ApiModelProperty(value = "是否已经全部上传 0否1是")
    private Integer status;

    @ApiModelProperty(value="银行code")
    private String bankBranchId;

    @ApiModelProperty(value="银行名称")
    private String bankBranchName;
}
