package com.panda.web.param;



import lombok.Data;


@Data
public class UploadResponse {

    private String minIoUrl;

    private String nginxUrl;

    private String url;

    public UploadResponse(String minIoUrl, String nginxUrl) {
        this.minIoUrl = minIoUrl;
        this.nginxUrl = nginxUrl;
    }

    public UploadResponse(String url) {
        this.url = url;
    }
}