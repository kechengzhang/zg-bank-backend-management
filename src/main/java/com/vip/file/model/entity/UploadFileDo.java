package com.vip.file.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 【请填写功能名称】对象 upload_file
 *
 * @author
 * @date 2022-06-27
 */
@Data
@Accessors(chain = true)
@TableName("upload_file")
@ApiModel(value = "upload_file对象", description = "")
public class UploadFileDo implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "发生地")
    @TableField(value = "occurPlace")
    private String occurPlace;

    @ApiModelProperty(value = "合并后的地址")
    @TableField(value = "filePath")
    private String filePath;

    @ApiModelProperty(value = "是否删除0否1是")
    @TableField(value = "status")
    private String status;

    @ApiModelProperty(value = "事件唯一标识")
    @TableField(value = "event")
    private String event;

    @ApiModelProperty(value = "视频是否合成 0否1是")
    @TableField(value = "isCompose")
    private String isCompose;

    @ApiModelProperty(value = "回调ip")
    @TableField(value = "ip")
    private String ip;

    @ApiModelProperty(value = "回调port")
    @TableField(value = "port")
    private String port;


    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;

    @ApiModelProperty(value = "发生时间")
    @TableField(value = "occurTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date occurTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "银行id")
    @TableField(value = "bank_branch_id")
    private String bankBranchId;
    @ApiModelProperty(value = "银行名称")
    @TableField(value = "bank_branch_name")
    private String bankBranchName;
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "synthesis_date")
    private String synthesisDate;

}
