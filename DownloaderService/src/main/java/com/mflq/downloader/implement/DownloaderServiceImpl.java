package com.mflq.downloader.implement;

import com.mflq.downloader.handler.MyStompSessionHandler;
import com.mflq.downloader.model.FileStatus;
import com.mflq.downloader.service.DownloaderService;
import com.mflq.downloader.service.ProgressCallBackService;
import com.mflq.downloader.util.RBCWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

@Log4j2
@Service
public class DownloaderServiceImpl implements DownloaderService {

    @Autowired
    private ProgressCallBackService progressCallBackService;
    private MyStompSessionHandler myStompSessionHandler;


    public DownloaderServiceImpl() {
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:8080/ws";

        this.myStompSessionHandler = new MyStompSessionHandler();
        StompSessionHandler sessionHandler = this.myStompSessionHandler;
        stompClient.connectAsync(url, sessionHandler);

    }


    @Override
    public void downLoadFile(Path path, URLConnection urlConnection, long localFileSize) {


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
        /*Este muestra el progreso de descarga en este hilo*/
        this.progressCallBackService.rbcProgressCallback(sizeRead, progress);
        /*Envía un mensaje al servidor socket para notificar al cliente del progreso de descarga*/
        myStompSessionHandler.sendMessage(new FileStatus(sizeRead, progress));

    }

}

