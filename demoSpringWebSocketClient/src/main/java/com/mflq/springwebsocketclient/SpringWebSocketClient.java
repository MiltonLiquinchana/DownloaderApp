package com.mflq.springwebsocketclient;

import com.mflq.springwebsocketclient.handler.MyStompSessionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SpringBootApplication
public class SpringWebSocketClient {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebSocketClient.class, args);
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:8080/ws";


        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connectAsync(url, sessionHandler);


    }

}
