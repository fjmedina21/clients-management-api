package com.myCompany.models.apiResponses;

import org.jboss.resteasy.reactive.RestResponse;

public abstract class BaseApiResponse {

    private boolean ok;
    private int statusCode;
    private String detail;

    protected BaseApiResponse() {
        this(RestResponse.StatusCode.OK, null);
    }

    protected BaseApiResponse(int statusCode, String detail) {
        this.statusCode = statusCode;
        this.ok = statusCode < 400;
        this.detail = detail != null
                ? detail
                : defaultMessage(statusCode);
    }

    protected BaseApiResponse(String detail) {
        this.ok = true;
        this.statusCode = RestResponse.StatusCode.OK;
        this.detail = detail != null
                ? detail
                : defaultMessage(statusCode);
    }

    protected BaseApiResponse(int statusCode) {
        this.statusCode = statusCode;
        this.ok = statusCode < 400;
        this.detail = defaultMessage(statusCode);
    }

    protected static String defaultMessage(int statusCode) {
        return switch (statusCode) {
            // 2xx Success
            case 200 -> "The request was successful.";
            case 201 -> "resource created.";
            case 202 -> "The request has been accepted for processing, but not completed.";
            case 204 -> "The server successfully processed the request and is not returning any content.";

            // 4xx Client Errors
            case 400 -> "The server could not understand the request due to invalid syntax.";
            case 401 -> "Authentication is required to access this resource.";
            case 402 -> "Reserved for future use; used in digital payment systems.";
            case 403 -> "The client does not have access rights to the content.";
            case 404 -> "The server cannot find the requested resource.";

            // 5xx Server Errors
            case 500 -> "The server was unable to complete your request. Please try again later.";
            case 503 -> "The server is currently unavailable (overloaded or down). Please try again later.";
            case 504 -> "The server did not receive a timely response from an upstream server.";

            default -> "HTTP Status Code not defined";
        };
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}