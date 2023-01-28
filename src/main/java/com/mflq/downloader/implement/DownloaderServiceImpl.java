package com.mflq.downloader.implement;

import com.mflq.downloader.service.DownloaderService;
import com.mflq.downloader.service.ProgressCallBackService;
import com.mflq.downloader.util.RBCWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
public class DownloaderServiceImpl implements DownloaderService {

    @Autowired
    private ProgressCallBackService progressCallBackService;

    @Override
    public CompletableFuture<Boolean> downLoadFile(Path path, String remoteURL) throws IOException {

        URLConnection connection = new URL(remoteURL).openConnection();
        long localFileSize = 0;

        if (Files.exists(path)) {

            localFileSize = Files.size(path);

            if (localFileSize == connection.getContentLength()) {
                log.warn("El archivo ya fue descargado");

                return CompletableFuture.completedFuture(true);
            }
            connection.setRequestProperty("Range", "bytes=" + localFileSize + "-");

        } else {
            Files.createFile(path);
        }

        log.info("Download initialized");
        try (
                ReadableByteChannel rbc = new RBCWrapper(Channels.newChannel(connection.getInputStream()), localFileSize, (connection.getContentLength() + localFileSize), this);
                FileOutputStream fos = new FileOutputStream(path.toString(), true);
                FileChannel fileChannel = fos.getChannel()
        ) {
            fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);
            return CompletableFuture.completedFuture(true);

        } catch (IOException e) {
            log.error("Uh oh: " + e);
            return CompletableFuture.completedFuture(false);
        }
    }


    @Override
    public void llamada(long sizeRead, double progress) {
        this.progressCallBackService.rbcProgressCallback(sizeRead, progress);
    }

}

