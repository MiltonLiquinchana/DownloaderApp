package com.mflq.downloader.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadResponse {
    private String from;
    private String to;
    private long sizeRead;
    private double progress;

}
