package com.mflq.downloader.implement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.mflq.downloader.dto.DownloadRequest;
import com.mflq.downloader.dto.DownloadWebSocketMessage;
import com.mflq.downloader.handler.MyStompSessionHandler;
import com.mflq.downloader.service.DownloaderService;
import com.mflq.downloader.service.ProgressCallBackService;
import com.mflq.downloader.util.RBCWrapper;

import lombok.extern.log4j.Log4j2;

/**
 * Implementación del servicio de descarga de archivos.
 *
 * Esta clase se encarga de:
 * - Descargar archivos desde URLs remotas
 * - Notificar el progreso de descarga en tiempo real usando WebSockets
 * - Manejar la reanudación de descargas parciales
 * - Gestionar conexiones STOMP para comunicación bidireccional
 *
 * @author MFLQ
 * @version 1.0
 */
@Log4j2
@Service
public class DownloaderServiceImpl implements DownloaderService {

	/**
	 * Servicio para manejar callbacks de progreso de descarga.
	 * Se utiliza para notificar el avance de la descarga en el hilo actual.
	 */
	@Autowired
	private ProgressCallBackService progressCallBackService;

	/**
	 * Crea y configura un manejador STOMP para comunicación WebSocket.
	 *
	 * Este método establece una conexión WebSocket con el servidor para permitir
	 * la notificación en tiempo real del progreso de descarga al cliente.
	 *
	 * @param downloadRequest Objeto que contiene la información de la descarga,
	 *                       incluyendo el nombre del archivo
	 * @return MyStompSessionHandler configurado y conectado al servidor WebSocket
	 */
	private MyStompSessionHandler createMyStompSessionHandler(DownloadRequest downloadRequest) {
		log.info("Iniciando configuración de cliente STOMP para archivo: {}", downloadRequest.getFileName());

		// Crear cliente WebSocket estándar
		WebSocketClient client = new StandardWebSocketClient();

		// Crear cliente STOMP que usa el WebSocket como transporte
		WebSocketStompClient stompClient = new WebSocketStompClient(client);

		// Configurar conversor de mensajes para manejar JSON
		// Esto permite enviar y recibir objetos Java como mensajes JSON
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		// URL del endpoint WebSocket del servidor
		// TODO: Considerar externalizar esta URL a un archivo de configuración
		String url = "ws://localhost:8080/ws";

		// Crear manejador personalizado para la sesión STOMP
		// Este manejador se encarga de procesar mensajes entrantes y salientes
		MyStompSessionHandler myStompSessionHandler = new MyStompSessionHandler(downloadRequest.getFileName());

		// Convertir a la interfaz genérica StompSessionHandler
		StompSessionHandler sessionHandler = myStompSessionHandler;

		try {
			// Establecer conexión asíncrona con el servidor WebSocket
			// La conexión se maneja en un hilo separado para no bloquear
			stompClient.connectAsync(url, sessionHandler);
			log.info("Conexión STOMP iniciada exitosamente para: {}", downloadRequest.getFileName());
		} catch (Exception e) {
			log.error("Error al establecer conexión STOMP para {}: {}", downloadRequest.getFileName(), e.getMessage());
			// TODO: Implementar manejo de errores más robusto
		}

		return myStompSessionHandler;
	}


	/**
	 * Método principal para descargar un archivo desde una URL remota.
	 *
	 * Este método:
	 * 1. Configura un cliente WebSocket para notificaciones en tiempo real
	 * 2. Crea un canal de lectura que monitorea el progreso
	 * 3. Maneja la escritura del archivo con soporte para reanudación
	 * 4. Notifica el progreso a través de WebSocket
	 *
	 * @param path Ruta donde se guardará el archivo descargado
	 * @param urlConnection Conexión establecida con la URL del archivo
	 * @param localFileSize Tamaño actual del archivo local (para reanudación)
	 * @param downloadRequest Información de la solicitud de descarga
	 */
	@Override
	public void downLoadFile(Path path, URLConnection urlConnection, long localFileSize,
							 DownloadRequest downloadRequest) {

		log.info("Iniciando descarga del archivo: {} en ruta: {}", downloadRequest.getFileName(), path.toString());
		log.debug("Tamaño local actual: {} bytes, Tamaño total esperado: {} bytes",
				localFileSize, urlConnection.getContentLength());

		// Crear y configurar el manejador STOMP para notificaciones
		MyStompSessionHandler myStompSessionHandler = createMyStompSessionHandler(downloadRequest);

		// Usar try-with-resources para garantizar el cierre automático de recursos
		try (
				// Canal de lectura envuelto para monitorear progreso
				// RBCWrapper notifica el progreso de descarga mientras lee los datos
				ReadableByteChannel rbc = new RBCWrapper(
						Channels.newChannel(urlConnection.getInputStream()),  // Canal de entrada desde la URL
						localFileSize,                                        // Bytes ya descargados
						(urlConnection.getContentLength() + localFileSize),   // Tamaño total esperado
						this,                                                 // Callback para notificaciones
						myStompSessionHandler,                                // Manejador WebSocket
						downloadRequest                                       // Información de la descarga
				);

				// Stream de salida para escribir al archivo local
				// El parámetro 'true' habilita el modo append para reanudación
				FileOutputStream fos = new FileOutputStream(path.toString(), true);

				// Canal de archivo para escritura eficiente
				FileChannel fileChannel = fos.getChannel()
		) {
			// Transferir datos del canal de lectura al archivo
			// Long.MAX_VALUE indica que se transfieran todos los bytes disponibles
			fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);

			log.info("Descarga completada exitosamente: {}", downloadRequest.getFileName());

		} catch (IOException e) {
			log.error("Error durante la descarga de {}: {}", downloadRequest.getFileName(), e.getMessage(), e);

			// TODO: Implementar lógica de recuperación o notificación de error
			// - Notificar al cliente sobre el error vía WebSocket
			// - Limpiar recursos parciales si es necesario
			// - Registrar el estado del error en la base de datos
		}
	}

	/**
	 * Notifica el progreso de descarga tanto localmente como a través de WebSocket.
	 *
	 * Este método es llamado periódicamente por RBCWrapper durante la descarga
	 * para informar sobre el avance del proceso.
	 *
	 * @param sizeRead Cantidad de bytes leídos hasta el momento
	 * @param progress Porcentaje de progreso (0.0 - 100.0)
	 * @param mysSessionHandler Manejador de sesión STOMP para envío de mensajes
	 * @param downloadRequest Información de la descarga en progreso
	 */
	@Override
	public void notifyDownloadProgres(long sizeRead, double progress, MyStompSessionHandler mysSessionHandler,
									  DownloadRequest downloadRequest) {

		log.debug("Progreso de descarga - Archivo: {}, Bytes leídos: {}, Progreso: {:.2f}%",
				downloadRequest.getFileName(), sizeRead, progress);

		// Notificar progreso en el hilo actual (para logging/monitoreo local)
		this.progressCallBackService.rbcProgressCallback(sizeRead, progress);

		// Crear mensaje de respuesta con la información de progreso
		DownloadWebSocketMessage downloadWebSocketMessage = new DownloadWebSocketMessage(
				downloadRequest.getFileName(),  // Nombre del archivo
				downloadRequest.getClient(),    // Cliente que solicitó la descarga
				sizeRead,                       // Bytes descargados
				progress                        // Porcentaje de progreso
		);

		try {
			// Enviar notificación al cliente a través de WebSocket
			mysSessionHandler.sendMessage(downloadWebSocketMessage);

		} catch (Exception e) {
			log.warn("Error al enviar notificación de progreso para {}: {}",
					downloadRequest.getFileName(), e.getMessage());

			// No interrumpir la descarga por errores de notificación
			// La descarga puede continuar aunque las notificaciones fallen
		}
	}
}