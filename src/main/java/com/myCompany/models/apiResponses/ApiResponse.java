package com.myCompany.models.apiResponses;

import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

public class ApiResponse<T> extends BaseApiResponse {

    private T data;

    public ApiResponse() {
        super();
    }

    public ApiResponse(int statusCode) {
        super(statusCode);
    }

    public ApiResponse(String detail) {
        super(detail);
    }

    public ApiResponse(T data) {
        super();
        this.data = data;
    }

    public ApiResponse(int statusCode, String detail) {
        super(statusCode, detail);
    }

    public ApiResponse(int statusCode, T data) {
        super(statusCode);
        this.data = data;
    }

    public ApiResponse(String detail, T data) {
        super(RestResponse.StatusCode.OK, detail);
        this.data = data;
    }

    public ApiResponse(int statusCode, String detail, T data) {
        super(statusCode, detail);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}