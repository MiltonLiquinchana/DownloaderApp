package com.mflq.downloader.service;

import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface DownloaderService {
    @Async("asyncExecutor")
    CompletableFuture<Boolean> downLoadFile(Path localPath, String remoteURL) throws IOException;

    void llamada(long sizeRead, double progress);
}
