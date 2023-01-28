package com.mflq.downloader.controller;

import com.mflq.downloader.model.DownloadRequest;
import com.mflq.downloader.service.DownloaderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;

@Log4j2
@RestController
public class DownloaderController {
    @Autowired
    private DownloaderService downloaderService;

    @PostMapping("download")
    public String downloadStart(@RequestBody DownloadRequest downloadRequest) throws IOException {

        downloaderService.downLoadFile(Path.of(downloadRequest.getFileOutputPath(), downloadRequest.getFileName()), downloadRequest.getUrlRequest());
        return "Descarga iniciada";
    }
}
