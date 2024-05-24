package com.gl.custom.model;


public class CustomApiResponse {
    String status;
    String message;
   public Result result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public CustomApiResponse() {
    }
    public CustomApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public CustomApiResponse(String status, String message, Result result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    @Override
    public String toString() {
        return "CustomApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}

