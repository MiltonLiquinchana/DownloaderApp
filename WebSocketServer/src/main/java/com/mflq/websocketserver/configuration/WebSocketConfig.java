package com.mflq.websocketserver.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /*Registramos un endpoint para conectarse a este servidor*/
        registry.addEndpoint("ws")
                /*Damos acceso desde cualquier servidor*/
                .setAllowedOriginPatterns("*");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /*Configuramos un endpoint por el cual el servidor podra enviar mensajes
         * */
        registry.enableSimpleBroker("/user");
        /*configuramos un endpoint por el cual el cliente nos enviara un mensaje*/
        registry.setApplicationDestinationPrefixes("/app");
        /*Registramos que enpoint de enablesimplebroker se va a usar para notificar
         * a un usario especifico*/
        registry.setUserDestinationPrefix("/user");
    }
}
