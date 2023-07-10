package com.vip.file.bean;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zkc
 * @date 2021/06/01
 * 分页
 *
 */
@Data
@ApiModel("分页")
public class PageData {
    @ApiModelProperty("当前页数")
    private int page;
    @ApiModelProperty("每页条数")
    private int size;
    @ApiModelProperty("总条数")
    private long total;
    @ApiModelProperty("返回数据")
    private List<? extends Object> rows;
    public PageData(PageInfo pageInfo){
        this.page = pageInfo.getPageNum();
        this.size = pageInfo.getPageSize();
        this.rows = pageInfo.getList();
        this.total = pageInfo.getTotal();
    }

    public PageData(int page,int size,long total,List<? extends Object> rows){
        this.page = page;
        this.size = size;
        this.rows = rows;
        this.total = total;
    }
}
