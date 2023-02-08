package com.mflq.downloader.service;

import java.net.URLConnection;
import java.nio.file.Path;

import org.springframework.scheduling.annotation.Async;

import com.mflq.downloader.handler.MyStompSessionHandler;
import com.mflq.downloader.model.DownloadRequest;

public interface DownloaderService {
    @Async("asyncExecutor")
    void downLoadFile(Path localPath, URLConnection urlConnection, long localFileSize,DownloadRequest downloadRequest);

    void notifyDownloadProgres(long sizeRead, double progress,MyStompSessionHandler mysSessionHandler,DownloadRequest downloadRequest);
//    void setDownloadRequest(DownloadRequest downloadRequest);
}
