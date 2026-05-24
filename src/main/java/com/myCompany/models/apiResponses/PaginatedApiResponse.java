package com.myCompany.models.apiResponses;

import com.myCompany.helpers.PagedList;
import org.jboss.resteasy.reactive.RestResponse;

public class PaginatedApiResponse<T> extends BaseApiResponse {

    private final int currentPage;
    private final int totalPages;
    private final int pageCount;
    private final int pageSize;
    private final int totalCount;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final PagedList<T> data;

    public PaginatedApiResponse(PagedList<T> data) {
        super(RestResponse.StatusCode.OK);
        this.currentPage = data.getCurrentPage();
        this.totalPages = data.getTotalPages();
        this.pageCount = data.getPageCount();
        this.pageSize = data.getPageSize();
        this.totalCount = data.getTotalCount();
        this.hasPrevious = data.hasPrevious();
        this.hasNext = data.hasNext();
        this.data = data;
    }

    public PagedList<T> getData() {
        return data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}