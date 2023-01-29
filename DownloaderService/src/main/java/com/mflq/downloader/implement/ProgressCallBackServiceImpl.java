package com.mflq.downloader.implement;

import com.mflq.downloader.handler.MyStompSessionHandler;
import com.mflq.downloader.model.FileStatus;
import com.mflq.downloader.service.ProgressCallBackService;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Log4j2
@Service
public class ProgressCallBackServiceImpl implements ProgressCallBackService {



    @Override
    public void rbcProgressCallback(long sizeRead, double progress) {

        log.info("download progress {} Megabytes received, {}%", sizeRead, String.format("%.02f", progress));


    }
}
