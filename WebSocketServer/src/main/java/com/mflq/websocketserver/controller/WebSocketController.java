package com.mflq.websocketserver.controller;

import com.mflq.websocketserver.model.FileStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /*con Message mapping indicamos una ruta por la cual el cliente nos manda el mensaje,
     * quedara asi /app/message, en la cual /app corresponderá a la ruta que configuramos, /message
     * sería un agregado a la ruta*/
    @MessageMapping("/message")
    public void sendNotification(@Payload FileStatus fileStatus) {
        log.info("Enviando notificación to: {}", fileStatus.getTo());
        simpMessagingTemplate.convertAndSendToUser(fileStatus.getTo(), "/queue/notification", fileStatus);

    }
}
