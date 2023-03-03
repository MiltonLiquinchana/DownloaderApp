package com.mflq.downloader.handler;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.mflq.downloader.dto.DownloadResponse;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MyStompSessionHandler extends StompSessionHandlerAdapter {
	private StompSession session;
	private String downloadClientName;

	public MyStompSessionHandler(String downloadClientName) {
		this.downloadClientName = downloadClientName;
	}

	/* Después de conectar */
	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		log.debug("Subscribiendo con el usuario {} afterConnected", this.downloadClientName);
		// Nos subscribimos a un endpoint para escuchar los mensajes
		session.subscribe("/user/" + this.downloadClientName + "/queue/notification", this);
		this.session = session;

	}

	/* En caso de que suceda un error */
	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		log.error("Got an exception", exception);
	}

	/* Este metodo establece el tipo de mensaje saliente */
	@Override
	public Type getPayloadType(StompHeaders headers) {
		log.debug("Estableciendo typos de mensajes salientes getPayloadType");

		/* Por el momento el mensaje es de tipo DownloadResponse.class */
		return DownloadResponse.class;
	}

	/* Este metodo maneja los mensajes recividos */
	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		DownloadResponse msg = (DownloadResponse) payload;
		log.debug("Mensaje recivido: {}", msg);
	}

	public void sendMessage(DownloadResponse downloadResponse) {
		log.debug("Notificando al cliente");
		this.session.send("/app/message", downloadResponse);
	}
}
