package com.mflq.downloader.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mflq.downloader.dto.DownloadContructorRequest;
import com.mflq.downloader.dto.DownloadContructorResponse;
import com.mflq.downloader.dto.DownloadRequest;
import com.mflq.downloader.service.DownloaderService;

import lombok.extern.log4j.Log4j2;

/**
 * Controlador REST para manejar las operaciones de descarga de archivos.
 * Proporciona endpoints para iniciar descargas con capacidad de reanudación.
 *
 * @author MFLQ
 * @version 1.0
 */
@Log4j2
@RestController
@RequestMapping("downloader")
public class DownloaderController {

	/**
	 * Servicio encargado de la lógica de descarga de archivos.
	 */
	@Autowired
	private DownloaderService downloaderService;

	/**
	 * Endpoint para iniciar la descarga de un archivo desde una URL específica.
	 * Soporta reanudación de descargas parciales mediante HTTP Range requests.
	 *
	 * @param downloadRequest Objeto que contiene la URL del archivo, ruta de destino y nombre del archivo
	 * @return String con el estado de la operación (descarga iniciada o archivo ya existe)
	 * @throws IOException Si ocurre un error de E/O durante la creación del archivo o conexión
	 * @throws URISyntaxException Si la URL proporcionada tiene un formato inválido
	 */
	@PostMapping("download")
	public String downloadStart(@RequestBody DownloadRequest downloadRequest) throws IOException, URISyntaxException {

		// Crea un objeto URL a partir de la cadena de texto recibida en la petición
		URL url = new URL(downloadRequest.getUrlRequest());

		// Establece la conexión inicial con el servidor remoto para obtener metadatos del archivo
		URLConnection connection = url.openConnection();


		// Construye la ruta completa del archivo local combinando el directorio de destino y el nombre del archivo
		Path path = Path.of(downloadRequest.getFileOutputPath(), downloadRequest.getFileName());

		// Inicializa el contador de bytes del archivo local (usado para descargas parciales)
		long localFileSize = 0;

		// Verifica si el archivo ya existe en el sistema de archivos local
		if (Files.exists(path)) {
			// Si existe, obtiene el tamaño actual para determinar cuántos bytes faltan por descargar
			localFileSize = Files.size(path);
		} else {
			// Si no existe, crea un nuevo archivo vacío en la ruta especificada
			Files.createFile(path);
		}

		// Compara el tamaño del archivo local con el tamaño del archivo remoto
		if (localFileSize == connection.getContentLength()) {
			// Si los tamaños coinciden, la descarga ya está completa
			log.warn("El archivo '{}' ya fue descargado completamente", downloadRequest.getFileName());
			return "El archivo ya fue descargado";
		}

		// Restablece la conexión para configurar la descarga parcial
		// (necesario porque ya se consumió la conexión anterior para obtener metadatos)
		connection = url.openConnection();

		// Configura el header HTTP Range para solicitar solo los bytes faltantes
		// Esto permite reanudar descargas interrumpidas desde el punto donde se quedaron
		connection.setRequestProperty("Range", "bytes=" + localFileSize + "-");

		// Delega la lógica de descarga al servicio especializado
		downloaderService.downLoadFile(path, connection, localFileSize, downloadRequest);

		// Confirma que el proceso de descarga ha comenzado exitosamente
		return "Descarga iniciada";
	}
}