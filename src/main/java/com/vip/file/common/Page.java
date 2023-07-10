package com.vip.file.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Page implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long totalCount;

    /**
     * 当前页
     */
    private int currentPage = 1;

    /**
     * 每页记录数
     */
    private int perPageSize;
    
    private List<Map<String, Object>> list;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public int getPerPageSize() {
		return perPageSize;
	}

	public void setPerPageSize(int perPageSize) {
		this.perPageSize = perPageSize;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "Page [totalCount=" + totalCount + ", currentPage=" + currentPage + ", perPageSize=" + perPageSize
				+ ", list=" + list + "]";
	}

}
