package com.mflq.downloader.service;

import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;

public interface DownloaderService {
    @Async("asyncExecutor")
    void downLoadFile(Path localPath, URLConnection urlConnection,long localFileSize) throws IOException;

    void notifyDownloadProgres(long sizeRead, double progress);
}
