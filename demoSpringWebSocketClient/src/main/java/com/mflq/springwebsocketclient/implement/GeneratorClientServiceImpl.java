package com.mflq.springwebsocketclient.implement;

import com.mflq.springwebsocketclient.handler.MyStompSessionHandler;
import com.mflq.springwebsocketclient.service.GeneratorClientService;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Log4j2
@Service
public class GeneratorClientServiceImpl implements GeneratorClientService {
    @Override
    public void generateClient(String userSubscribeName) {
        log.info("Generando cliente: {}", userSubscribeName);
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:8080/ws";


        StompSessionHandler sessionHandler = new MyStompSessionHandler(userSubscribeName);
        stompClient.connectAsync(url, sessionHandler);


    }
}
