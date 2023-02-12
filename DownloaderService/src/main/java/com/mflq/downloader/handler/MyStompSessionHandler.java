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

	@Override
	public Type getPayloadType(StompHeaders headers) {
		/* Por el momento el mensaje es de tipo string */
		return DownloadResponse.class;
	}

	/* Este metodo maneja los mensajes recividos */
	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		DownloadResponse msg = (DownloadResponse) payload;
		log.info("Mensaje recivido: ***{}", msg);
	}

	public void sendMessage(DownloadResponse downloadResponse) {
		this.session.send("/app/message", downloadResponse);
	}
}
