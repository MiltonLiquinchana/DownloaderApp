package com.mflq.downloader.service;

import org.springframework.scheduling.annotation.Async;

import java.net.URLConnection;
import java.nio.file.Path;

public interface DownloaderService {
    @Async("asyncExecutor")
    void downLoadFile(Path localPath, URLConnection urlConnection, long localFileSize);

    void notifyDownloadProgres(long sizeRead, double progress);
}
