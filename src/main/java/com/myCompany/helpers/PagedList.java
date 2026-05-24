package com.myCompany.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PagedList<T> extends ArrayList<T> {
    private final int currentPage;
    private final int totalPages;
    private final int pageCount;
    private final int pageSize;
    private final int totalCount;

    public PagedList(List<T> items, int totalCount, int currentPage, int pageCount, int pageSize) {
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageCount = pageCount;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil(totalCount / (double) pageSize);
        addAll(items);
    }

    public static <T> PagedList<T> toPagedList(Collection<T> source, int currentPage, int pageSize) {
        int totalCount = source.size();

        List<T> items = source.stream()
                .skip((long) (currentPage - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toCollection(ArrayList::new));

        return new PagedList<T>(items, totalCount, currentPage, items.size(), pageSize);
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

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }
}
