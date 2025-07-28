package com.mflq.websocketserver.controller;

import com.mflq.websocketserver.model.WebSocketMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Controlador WebSocket que maneja la comunicación en tiempo real entre usuarios.
 *
 * Este controlador procesa los mensajes STOMP enviados por los clientes WebSocket
 * y los reenvía a los destinatarios correspondientes utilizando el sistema de
 * mensajería de Spring WebSocket.
 *
 * ENDPOINTS DISPONIBLES:
 * - /app/message → Maneja mensajes privados entre usuarios (implementado)
 * - Puedes agregar más endpoints como /app/chat, /app/status, etc.
 *
 * FLUJO DE MENSAJERÍA:
 * Cliente A → /app/message → Servidor procesa → /user/usuarioB/queue/notification → Cliente B
 */
@Log4j2
@Controller
public class WebSocketController {

    /**
     * Template de mensajería STOMP para enviar mensajes a usuarios específicos.
     *
     * SimpMessagingTemplate proporciona métodos para enviar mensajes a destinos
     * específicos, incluyendo la capacidad de enviar mensajes privados a usuarios
     * individuales a través del sistema de routing de Spring WebSocket.
     */
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * ENDPOINT: /app/message - Maneja mensajes privados entre usuarios
     *
     * Este método actúa como un "cartero inteligente" que:
     * 1. Recibe mensajes de cualquier cliente en la ruta /app/message
     * 2. Lee quién es el destinatario (message.getTo())
     * 3. Reenvía el mensaje SOLO al usuario destinatario
     * 4. El destinatario debe estar suscrito a su cola personal para recibirlo
     *
     * RUTA COMPLETA: /app/message
     * - "/app" viene del prefijo configurado en WebSocketConfig
     * - "/message" es el endpoint específico definido en @MessageMapping
     *
     * TIPOS DE MENSAJES QUE PUEDE MANEJAR:
     * - Notificaciones de progreso de transferencia
     * - Mensajes de estado entre usuarios
     * - Actualizaciones de progreso de operaciones
     * - Cualquier comunicación privada entre dos usuarios
     *
     * EJEMPLO DE USO:
     * Cliente JavaScript envía:
     * stompClient.send("/app/message", {}, JSON.stringify({
     *     from: "juan", to: "maria", sizeRead: 1024, progress: 0.5
     * }));
     *
     * Resultado: Solo María recibe el mensaje en /user/maria/queue/notification
     *
     * @param message El mensaje recibido del cliente, deserializado automáticamente
     *                desde JSON a WebSocketMessage. Contiene información del
     *                remitente (from), destinatario (to) y datos asociados
     *                (sizeRead, progress).
     */
    @MessageMapping("/message")  // ENDPOINT: Ruta completa será /app/message
    public void sendNotification(@Payload WebSocketMessage message) {
        log.info("📨 MENSAJE RECIBIDO - De: '{}' → Para: '{}' | Progreso: {}",
                message.getFrom(), message.getTo(), message.getProgress());

        // PASO CRÍTICO: Reenvío del mensaje al usuario específico
        //
        // ¿Cómo funciona convertAndSendToUser()?
        // 1. Toma el username del destinatario: "maria"
        // 2. Construye automáticamente la ruta: "/user/maria/queue/notification"
        // 3. El broker busca SOLO las sesiones WebSocket de "maria"
        // 4. Envía el mensaje únicamente a esas sesiones (mensajería privada)
        //
        // REQUISITO: El usuario destinatario debe estar suscrito a su cola personal:
        // "stompClient.subscribe("/user/maria/queue/notification", function(message) {...});"
        simpMessagingTemplate.convertAndSendToUser(
                message.getTo(),                    // 👤 Usuario destinatario (ej: "maria")
                "/queue/notification",              // 📮 Cola específica del usuario
                message                            // 📦 Payload completo del mensaje
        );

        log.debug("✅ Mensaje enviado exitosamente → Usuario: {}", message.getTo());
    }
}