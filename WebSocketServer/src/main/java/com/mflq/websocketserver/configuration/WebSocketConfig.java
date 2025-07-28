package com.mflq.websocketserver.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración de WebSocket para habilitar comunicación bidireccional en tiempo real.
 *
 * Esta clase configura el servidor WebSocket utilizando STOMP (Simple Text Oriented Messaging Protocol)
 * sobre WebSocket, permitiendo el intercambio de mensajes entre el servidor y múltiples clientes.
 *
 * CONCEPTOS CLAVE:
 * - RUTAS DE CONEXIÓN: Donde los clientes se conectan inicialmente (/ws)
 * - RUTAS DE ENTRADA: Donde los clientes ENVÍAN mensajes al servidor (/app/*)
 * - RUTAS DE SALIDA: Donde el servidor PUBLICA mensajes para los clientes (/user/*)
 * - ENDPOINTS: Diferentes "buzones" para manejar tipos específicos de mensajes
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Registra los endpoints STOMP que los clientes utilizarán para establecer
     * la conexión WebSocket inicial.
     *
     * RUTA DE CONEXIÓN INICIAL:
     * Los clientes deben conectarse PRIMERO a esta ruta antes de poder
     * enviar o recibir mensajes. Es como la "puerta de entrada" al servidor.
     *
     * Ejemplo de conexión del cliente:
     * - URL completa: ws://localhost:8080/ws
     * - Después de conectarse, el cliente puede enviar a /app/* y suscribirse a /user/*
     *
     * @param registry El registro donde se configuran los endpoints STOMP
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra el endpoint "/ws" como punto de conexión para clientes WebSocket
        registry.addEndpoint("ws")
                // Permite conexiones desde cualquier origen (CORS habilitado para todos los dominios)
                // NOTA: En producción, considerar restringir a dominios específicos por seguridad
                .setAllowedOriginPatterns("*");
    }

    /**
     * Configura el message broker que manejará el enrutamiento de mensajes
     * entre el servidor y los clientes conectados.
     *
     * @param registry El registro del message broker a configurar
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        /* ═══════════════════════════════════════════════════════════════════════════════
         * 1. CONFIGURACIÓN DEL BROKER DE MENSAJES (SERVIDOR → CLIENTE)
         * ═══════════════════════════════════════════════════════════════════════════════ */

        // Habilita un message broker simple en memoria que actuará como intermediario
        // para distribuir mensajes a los clientes suscritos.
        //
        // ¿Qué hace exactamente?
        // - Mantiene una lista de clientes suscritos a diferentes "temas" o "colas"
        // - Cuando el servidor envía un mensaje a un tema, el broker lo distribuye
        //   automáticamente a TODOS los clientes suscritos a ese tema
        //
        // Ejemplo práctico:
        // - Cliente A se suscribe a "/user/juan/queue/notification"
        // - Cliente B se suscribe a "/user/maria/queue/notification"
        // - Cuando el servidor envía un mensaje a "/user/juan/queue/notification",
        //   solo Cliente A lo recibe (no Cliente B)
        registry.enableSimpleBroker("/user");

        /* ═══════════════════════════════════════════════════════════════════════════════
         * 2. CONFIGURACIÓN DE RUTAS DE APLICACIÓN (CLIENTE → SERVIDOR)
         * ═══════════════════════════════════════════════════════════════════════════════ */

        /* ═══════════════════════════════════════════════════════════════════════════════
         * 2. CONFIGURACIÓN DE RUTAS DE APLICACIÓN (CLIENTE → SERVIDOR)
         * ═══════════════════════════════════════════════════════════════════════════════ */

        // Define que TODOS los mensajes enviados por clientes que comiencen con "/app"
        // son considerados "mensajes de aplicación" y serán procesados por nuestros
        // controladores (métodos con @MessageMapping).
        //
        // ENDPOINTS DISPONIBLES (cada uno maneja un tipo diferente de mensaje):
        // - /app/message     → Mensajes privados entre usuarios
        // - /app/chat        → Mensajes de chat grupal
        // - /app/status      → Actualizaciones de estado
        // - /app/file-upload → Subida de archivos
        // - /app/notification → Notificaciones del sistema
        //
        // Flujo de procesamiento:
        // 1. Cliente envía mensaje a "/app/message"
        // 2. Spring WebSocket busca un método con @MessageMapping("/message")
        // 3. Ejecuta ese método con el contenido del mensaje como parámetro
        // 4. El método puede procesar, validar y/o reenviar el mensaje a otros usuarios
        //
        // IMPORTANTE: Sin este prefijo, los mensajes irían directamente al broker
        // sin pasar por nuestros controladores (no habría procesamiento personalizado)
        registry.setApplicationDestinationPrefixes("/app");

        /* ═══════════════════════════════════════════════════════════════════════════════
         * 3. CONFIGURACIÓN DE MENSAJES PRIVADOS (SERVIDOR → USUARIO ESPECÍFICO)
         * ═══════════════════════════════════════════════════════════════════════════════ */

        // Configura el sistema de mensajería privada a usuarios específicos.
        //
        // ¿Cómo funciona el envío privado?
        // Cuando usamos: simpMessagingTemplate.convertAndSendToUser("juan", "/queue/notification", mensaje)
        //
        // Spring automáticamente:
        // 1. Toma el username "juan"
        // 2. Antepone el prefijo "/user/"
        // 3. Construye la ruta final: "/user/juan/queue/notification"
        // 4. Envía el mensaje SOLO a las sesiones WebSocket asociadas con el usuario "juan"
        //
        // Esto permite que cada usuario reciba únicamente sus mensajes personales,
        // sin que otros usuarios puedan verlos.
        registry.setUserDestinationPrefix("/user");
    }
}