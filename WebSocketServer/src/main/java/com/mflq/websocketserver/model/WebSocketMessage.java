package com.mflq.websocketserver.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Modelo que representa un mensaje intercambiado entre usuarios a través de WebSocket.
 * Esta clase encapsula la información básica de comunicación entre usuarios,
 * incluyendo remitente, destinatario y datos de estado o progreso asociados.
 * Puede utilizarse para diferentes tipos de notificaciones en tiempo real.
 */
@Getter
@Setter
public class WebSocketMessage {

    /**
     * Identificador del usuario que envía el mensaje.
     * Puede ser un username, ID de usuario o identificador único del remitente.
     */
    private String from;

    /**
     * Identificador del usuario que recibe el mensaje.
     * Puede ser un username, ID de usuario o identificador único del destinatario.
     */
    private String to;

    /**
     * Cantidad de datos procesados o bytes leídos.
     * Este campo puede representar el tamaño de datos transferidos,
     * bytes procesados, o cualquier métrica numérica relevante.
     */
    private long sizeRead;

    /**
     * Valor de progreso o estado expresado como decimal.
     * Puede representar un porcentaje (0.0 - 1.0), un estado numérico,
     * o cualquier valor decimal que indique el avance de una operación.
     */
    private double progress;


}