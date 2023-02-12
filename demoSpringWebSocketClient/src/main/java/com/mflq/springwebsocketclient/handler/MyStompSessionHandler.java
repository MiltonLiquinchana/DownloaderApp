package com.mflq.springwebsocketclient.handler;

import com.mflq.springwebsocketclient.model.FileStatus;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

@Log4j2
@AllArgsConstructor
public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private String userSubscribeName;


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/user/" + userSubscribeName + "/queue/notification", this);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return FileStatus.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        FileStatus msg = (FileStatus) payload;
        log.info("progreso de descarga: {}", msg.getProgress());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Got an exception", exception);
    }
}