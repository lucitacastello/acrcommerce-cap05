package com.acrdev.acrcommerce.dto;

import java.time.Instant;

public class CustomErrorDTO {
    /*
    {
    "timestamp": "2023-06-05T17:56:42.645+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/products/26"
}
     */
    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;

    public CustomErrorDTO(Instant timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
