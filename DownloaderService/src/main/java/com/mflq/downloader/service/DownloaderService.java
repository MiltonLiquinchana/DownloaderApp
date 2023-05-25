package com.mflq.downloader.service;

import com.mflq.downloader.dto.DownloadRequest;
import com.mflq.downloader.handler.MyStompSessionHandler;
import org.springframework.scheduling.annotation.Async;

import java.net.URLConnection;
import java.nio.file.Path;

public interface DownloaderService {
    @Async("asyncExecutor")
    void downLoadFile(Path localPath, URLConnection urlConnection, long localFileSize, DownloadRequest downloadRequest);

    void notifyDownloadProgres(long sizeRead, double progress, MyStompSessionHandler mysSessionHandler,
                               DownloadRequest downloadRequest);
}
