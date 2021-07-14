package com.lyloou.component.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * Page Query Param
 *
 * @author jacky
 */
public abstract class PageQuery extends Query {

    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    private static final int DEFAULT_PAGE_SIZE = 10;

    @ApiModelProperty(value = "页大小，默认10")
    private int pageSize = DEFAULT_PAGE_SIZE;

    @ApiModelProperty(value = "页数，默认1")
    private int pageNum = 1;

    @ApiModelProperty(value = "排序字段")
    private String orderBy;

    @ApiModelProperty(value = "排序方向")
    private String orderDirection = ASC;

    @ApiModelProperty(value = "是否需要总数")
    private boolean needTotalCount = true;

    public int getPageNum() {
        if (pageNum < 1) {
            return 1;
        }
        return pageNum;
    }

    public PageQuery setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public int getPageSize() {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    public PageQuery setPageSize(int pageSize) {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
        return this;
    }


    @ApiModelProperty(hidden = true)
    public int getOffset() {
        return (getPageNum() - 1) * getPageSize();
    }

    public String getOrderBy() {
        return orderBy;
    }

    public PageQuery setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public PageQuery setOrderDirection(String orderDirection) {
        if (ASC.equalsIgnoreCase(orderDirection) || DESC.equalsIgnoreCase(orderDirection)) {
            this.orderDirection = orderDirection;
        }
        return this;
    }

    public boolean isNeedTotalCount() {
        return needTotalCount;
    }

    public void setNeedTotalCount(boolean needTotalCount) {
        this.needTotalCount = needTotalCount;
    }

    /**
     * 用于缓存的查询键
     *
     * @return 结果
     */
    @ApiModelProperty(hidden = true)
    public String getCachePageKey() {
        return "{" +
                "pageIndex=" + pageNum +
                ", pageSize=" + pageSize +
                ", orderBy='" + orderBy + '\'' +
                ", orderDirection='" + orderDirection + '\'' +
                ", needTotalCount=" + needTotalCount +
                '}';
    }
}
