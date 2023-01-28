package com.mflq.downloader.controller;

import com.mflq.downloader.model.DownloadRequest;
import com.mflq.downloader.service.DownloaderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
@RestController
public class DownloaderController {
    @Autowired
    private DownloaderService downloaderService;

    @PostMapping("download")
    public String downloadStart(@RequestBody DownloadRequest downloadRequest) throws IOException, URISyntaxException {
        URL url=new URL(downloadRequest.getUrlRequest());
        URLConnection connection = url.openConnection();
        Path path = Path.of(downloadRequest.getFileOutputPath(), downloadRequest.getFileName());
        long localFileSize = 0;

        if (Files.exists(path)) {
            localFileSize = Files.size(path);
        } else {
            Files.createFile(path);
        }

        if (localFileSize == connection.getContentLength()) {
            log.warn("El archivo ya fue descargado");
            return "El archivo ya fue descargado";

        }

        connection= url.openConnection();
        connection.setRequestProperty("Range", "bytes=" + localFileSize + "-");

        downloaderService.downLoadFile(path, connection, localFileSize);
        return "Descarga iniciada";
    }
}
