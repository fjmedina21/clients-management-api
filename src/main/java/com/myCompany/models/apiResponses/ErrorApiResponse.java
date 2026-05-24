package com.myCompany.models.apiResponses;

import java.util.Collections;
import java.util.List;

public class ErrorApiResponse extends BaseApiResponse {

    private String errorType;
    private List<String> errors;

    public ErrorApiResponse(int statusCode, String errorType) {
        super(statusCode);
        this.errors = Collections.emptyList();
        this.errorType = errorType;
    }

    public ErrorApiResponse(int statusCode, String errorType, List<String> errors) {
        super(statusCode);
        this.errors = errors;
        this.errorType = errorType;
    }

    public ErrorApiResponse(int statusCode, String detail, String errorType, List<String> errors) {
        super(statusCode, detail);
        this.errors = errors;
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}