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
    public void downLoadFile(Path path, URLConnection urlConnection,long localFileSize) throws IOException {



        log.info("Download initialized");
        try (
                ReadableByteChannel rbc = new RBCWrapper(Channels.newChannel(urlConnection.getInputStream()), localFileSize, (urlConnection.getContentLength() + localFileSize), this);
                FileOutputStream fos = new FileOutputStream(path.toString(), true);
                FileChannel fileChannel = fos.getChannel()
        ) {
            fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);

        } catch (IOException e) {
            log.error("Uh oh: " + e);
        }
    }


    @Override
    public void notifyDownloadProgres(long sizeRead, double progress) {
        this.progressCallBackService.rbcProgressCallback(sizeRead, progress);
    }

}

