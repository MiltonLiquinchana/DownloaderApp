package com.mflq.downloader.implement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.mflq.downloader.dto.DownloadContructorRequest;
import com.mflq.downloader.dto.DownloadContructorResponse;
import com.mflq.downloader.dto.DownloadRequest;
import com.mflq.downloader.dto.DownloadResponse;
import com.mflq.downloader.handler.MyStompSessionHandler;
import com.mflq.downloader.model.DownloadFileProjection;
import com.mflq.downloader.repository.DownloadFileRepository;
import com.mflq.downloader.service.DownloaderService;
import com.mflq.downloader.service.ProgressCallBackService;
import com.mflq.downloader.util.RBCWrapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DownloaderServiceImpl implements DownloaderService {

	@Autowired
	private ProgressCallBackService progressCallBackService;

	/* Creamos una referencia hacia el repositorio de datos */
	@Autowired
	private DownloadFileRepository downloadFileRepository;

	/* metodo para crear clientes stomp, para cada descarga */
	private MyStompSessionHandler createMyStompSessionHandler(DownloadRequest downloadRequest) {

		WebSocketClient client = new StandardWebSocketClient();

		WebSocketStompClient stompClient = new WebSocketStompClient(client);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		String url = "ws://localhost:8080/ws";

		MyStompSessionHandler myStompSessionHandler = new MyStompSessionHandler(downloadRequest.getFileName());
		StompSessionHandler sessionHandler = myStompSessionHandler;
		stompClient.connectAsync(url, sessionHandler);
		return myStompSessionHandler;

	}

	@Override
	public DownloadContructorResponse downloadContructor(DownloadContructorRequest downloadContructorRequest) {

		DownloadFileProjection downloadFileProjection = downloadFileRepository.getDownloadFileById(1);
		/* Creamos un Objeto */
		return new DownloadContructorResponse(downloadFileProjection.getUrl(),
				downloadFileProjection.getDownloadFileName(), downloadFileProjection.getDownloadFileLength(),
				downloadFileProjection.getDownloadFileDescription(), downloadFileProjection.getDownloadFilestatus(),
				downloadFileProjection.getFileType(), downloadFileProjection.getCategory(),
				downloadFileProjection.getCategoryFileOutputPath(), new ArrayList<>());
	}

	@Override
	public void downLoadFile(Path path, URLConnection urlConnection, long localFileSize,
			DownloadRequest downloadRequest) {

		log.info("Download initialize, {}", path.getFileName());
		MyStompSessionHandler myStompSessionHandler = createMyStompSessionHandler(downloadRequest);
		try (ReadableByteChannel rbc = new RBCWrapper(Channels.newChannel(urlConnection.getInputStream()),
				localFileSize, (urlConnection.getContentLength() + localFileSize), this, myStompSessionHandler,
				downloadRequest);
				FileOutputStream fos = new FileOutputStream(path.toString(), true);
				FileChannel fileChannel = fos.getChannel()) {
			fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);

		} catch (IOException e) {
			log.error("Uh oh: " + e);
		}
	}

	@Override
	public void notifyDownloadProgres(long sizeRead, double progress, MyStompSessionHandler mysSessionHandler,
			DownloadRequest downloadRequest) {
		/* Este muestra el progreso de descarga en este hilo */
		this.progressCallBackService.rbcProgressCallback(sizeRead, progress);
		/*
		 * Envía un mensaje al servidor socket para notificar al cliente del progreso de
		 * descarga
		 */
		mysSessionHandler.sendMessage(
				new DownloadResponse(downloadRequest.getFileName(), downloadRequest.getClient(), sizeRead, progress));

	}

}
