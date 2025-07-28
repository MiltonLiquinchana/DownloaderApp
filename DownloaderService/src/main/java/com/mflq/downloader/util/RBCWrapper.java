package com.mflq.downloader.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import com.mflq.downloader.dto.DownloadRequest;
import com.mflq.downloader.handler.MyStompSessionHandler;
import com.mflq.downloader.service.DownloaderService;

import lombok.extern.log4j.Log4j2;

/**
 * Wrapper para ReadableByteChannel que monitorea el progreso de descarga.
 *
 * Esta clase implementa el patrón Decorator/Wrapper para interceptar las operaciones
 * de lectura de un ReadableByteChannel y proporcionar funcionalidades adicionales:
 *
 * - Seguimiento del progreso de descarga en tiempo real
 * - Cálculo automático del porcentaje de descarga
 * - Notificación de progreso a través de callbacks
 * - Soporte para reanudación de descargas parciales
 *
 * Funciona como un "proxy" transparente que no modifica los datos, solo los monitorea.
 *
 * @author MFLQ
 * @version 1.0
 */
@Log4j2
public class RBCWrapper implements ReadableByteChannel {

	/**
	 * Servicio delegado para manejar las notificaciones de progreso.
	 * Se utiliza para callback cuando hay actualizaciones de progreso.
	 */
	private final DownloaderService delegate;

	/**
	 * Tamaño total del archivo en el servidor remoto (incluyendo lo ya descargado).
	 * Se usa como denominador para calcular el porcentaje de progreso.
	 */
	private final long sizeFileOnline;

	/**
	 * Canal de lectura original que se está envolviendo.
	 * Todas las operaciones de lectura se delegan a este canal.
	 */
	private final ReadableByteChannel readableByteChannel;

	/**
	 * Contador acumulativo de bytes leídos desde el inicio de esta sesión.
	 * Incluye bytes ya descargados previamente (para reanudación).
	 */
	private long sizeRead;

	/**
	 * Manejador de sesión STOMP para enviar notificaciones WebSocket.
	 * Permite comunicar el progreso al cliente en tiempo real.
	 */
	private final MyStompSessionHandler mysSessionHandler;

	/**
	 * Información de la solicitud de descarga.
	 * Contiene metadatos necesarios para las notificaciones.
	 */
	private final DownloadRequest downloadRequest;

	/**
	 * Constructor del wrapper de ReadableByteChannel con monitoreo de progreso.
	 *
	 * @param readableByteChannel Canal de lectura original a envolver
	 * @param sizeFileLocal Tamaño ya descargado localmente (para reanudación)
	 * @param sizeFileOnline Tamaño total del archivo remoto
	 * @param delegate Servicio para callbacks de progreso
	 * @param mysSessionHandler Manejador WebSocket para notificaciones
	 * @param downloadRequest Información de la solicitud de descarga
	 */
	public RBCWrapper(ReadableByteChannel readableByteChannel, long sizeFileLocal, long sizeFileOnline,
					  DownloaderService delegate, MyStompSessionHandler mysSessionHandler, DownloadRequest downloadRequest) {

		// Validaciones de entrada
		if (readableByteChannel == null) {
			throw new IllegalArgumentException("ReadableByteChannel no puede ser null");
		}
		if (delegate == null) {
			throw new IllegalArgumentException("DownloaderService delegate no puede ser null");
		}
		if (sizeFileOnline < 0) {
			throw new IllegalArgumentException("El tamaño del archivo online no puede ser negativo");
		}
		if (sizeFileLocal < 0) {
			throw new IllegalArgumentException("El tamaño del archivo local no puede ser negativo");
		}

		this.readableByteChannel = readableByteChannel;
		this.sizeRead = sizeFileLocal;  // Inicializar con bytes ya descargados
		this.sizeFileOnline = sizeFileOnline;
		this.delegate = delegate;
		this.mysSessionHandler = mysSessionHandler;
		this.downloadRequest = downloadRequest;

		log.info("RBCWrapper inicializado - Archivo: {}, Bytes previos: {}, Total: {}",
				downloadRequest != null ? downloadRequest.getFileName() : "unknown",
				sizeFileLocal, sizeFileOnline);
	}

	/**
	 * Cierra el canal de lectura subyacente.
	 *
	 * Delega la operación de cierre al canal original y libera recursos.
	 * Este método es llamado automáticamente en bloques try-with-resources.
	 *
	 * @throws IOException Si ocurre un error al cerrar el canal
	 */
	@Override
	public void close() throws IOException {
		log.debug("Cerrando RBCWrapper para archivo: {}",
				downloadRequest != null ? downloadRequest.getFileName() : "unknown");

		try {
			this.readableByteChannel.close();
			log.debug("Canal cerrado exitosamente");
		} catch (IOException e) {
			log.error("Error al cerrar el canal: {}", e.getMessage());
			throw e;
		}
	}

	/**
	 * Verifica si el canal de lectura está abierto.
	 *
	 * Delega la consulta al canal original sin modificaciones.
	 *
	 * @return true si el canal está abierto, false en caso contrario
	 */
	@Override
	public boolean isOpen() {
		boolean isOpen = this.readableByteChannel.isOpen();
		log.trace("Estado del canal - isOpen: {}", isOpen);
		return isOpen;
	}

	/**
	 * Lee datos del canal y monitorea el progreso de descarga.
	 *
	 * Este es el métodó principal donde ocurre la "magia" del monitoreo:
	 * 1. Delega la lectura al canal original
	 * 2. Si se leyeron bytes, actualiza el contador de progreso
	 * 3. Calcula el porcentaje de descarga
	 * 4. Notifica el progreso a través del servicio delegado
	 *
	 * @param bb Buffer donde se escribirán los datos leídos
	 * @return Número de bytes leídos, 0 si se alcanzó el final, -1 si el canal está cerrado
	 * @throws IOException Si ocurre un error durante la lectura
	 */
	@Override
	public int read(ByteBuffer bb) throws IOException {
		log.trace("Iniciando lectura de datos - Buffer capacity: {}", bb.capacity());

		int bytesRead;

		try {
			// Delegar la lectura real al canal subyacente
			bytesRead = this.readableByteChannel.read(bb);

			log.trace("Bytes leídos en esta operación: {}", bytesRead);

		} catch (IOException e) {
			log.error("Error durante la lectura: {}", e.getMessage());
			throw e;
		}

		// Solo procesar progreso si se leyeron datos
		if (bytesRead > 0) {
			// Actualizar contador total de bytes leídos
			this.sizeRead += bytesRead;

			// Calcular porcentaje de progreso
			double progress = calculateProgress();

			log.debug("Progreso actualizado - Total leído: {}/{} bytes ({:.2f}%)",
					this.sizeRead, this.sizeFileOnline, progress);

			try {
				// Notificar progreso a través del servicio delegado
				this.delegate.notifyDownloadProgres(
						this.sizeRead,
						progress,
						this.mysSessionHandler,
						this.downloadRequest
				);

			} catch (Exception e) {
				// No interrumpir la descarga por errores de notificación
				log.warn("Error al notificar progreso: {}", e.getMessage());
			}

		} else if (bytesRead == 0) {
			log.trace("No hay datos disponibles para lectura en este momento");
		} else {
			log.debug("Final del stream alcanzado (EOF)");
		}

		return bytesRead;
	}

	/**
	 * Calcula el porcentaje de progreso de la descarga.
	 *
	 * @return Porcentaje de progreso (0.0 - 100.0), o -1.0 si el tamaño total es desconocido
	 */
	private double calculateProgress() {
		if (this.sizeFileOnline <= 0) {
			log.trace("Tamaño de archivo desconocido, retornando progreso indeterminado");
			return -1.0;
		}

		double progress = ((double) this.sizeRead / (double) this.sizeFileOnline) * 100.0;

		// Asegurar que el progreso no exceda 100%
		return Math.min(progress, 100.0);
	}

	/**
	 * Obtiene el número total de bytes leídos hasta el momento.
	 *
	 * @return Cantidad de bytes leídos (incluyendo cualquier descarga previa)
	 */
	public long getBytesRead() {
		return this.sizeRead;
	}

	/**
	 * Obtiene el tamaño total esperado del archivo.
	 *
	 * @return Tamaño total del archivo en bytes
	 */
	public long getTotalSize() {
		return this.sizeFileOnline;
	}

	/**
	 * Verifica si la descarga ha sido completada.
	 *
	 * @return true si se han leído todos los bytes esperados
	 */
	public boolean isDownloadComplete() {
		return this.sizeFileOnline > 0 && this.sizeRead >= this.sizeFileOnline;
	}
}