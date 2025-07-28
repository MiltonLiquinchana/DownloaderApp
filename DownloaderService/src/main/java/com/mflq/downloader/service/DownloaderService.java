package com.mflq.downloader.service;

import java.net.URLConnection;
import java.nio.file.Path;

import org.springframework.scheduling.annotation.Async;
import com.mflq.downloader.dto.DownloadRequest;
import com.mflq.downloader.handler.MyStompSessionHandler;

public interface DownloaderService {

	@Async("asyncExecutor")
	void downLoadFile(Path localPath, URLConnection urlConnection, long localFileSize, DownloadRequest downloadRequest);

	void notifyDownloadProgres(long sizeRead, double progress, MyStompSessionHandler mysSessionHandler,
			DownloadRequest downloadRequest);
}
