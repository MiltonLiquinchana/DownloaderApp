package com.mflq.downloader.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadRequest {
    private String client;

    private String urlRequest;
    private String fileName;
    private String fileOutputPath;

}
