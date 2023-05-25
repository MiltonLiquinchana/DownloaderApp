package com.mflq.downloader.controller;

import com.mflq.downloader.dto.DownloadRequest;
import com.mflq.downloader.service.DownloaderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
@RestController
@RequestMapping("downloader")
public class DownloaderController {
    @Autowired
    private DownloaderService downloaderService;

    @PostMapping("download")
    public String downloadStart(@RequestBody DownloadRequest downloadRequest) throws IOException, URISyntaxException {
        /* Generamos un objeto URL con la url que recivimos */
        URL url = new URL(downloadRequest.getUrlRequest());
        /* Abrimos una conexion */
        URLConnection connection = url.openConnection();

        /*
         * Creamos un nuevo path con la ruta de guardado y el nombre del archivo a
         * descargar
         */
        Path path = Path.of(downloadRequest.getFileOutputPath(), downloadRequest.getFileName());

        /*
         * Variable para saber el tamaño del archivo local, en caso de ser necesario
         * reanudar descarga
         */
        long localFileSize = 0;

        /* Validamos la existencia de la ruta */
        if (Files.exists(path)) {
            /* Si la ruta existe obtiene el tamaño actual del archivo local */
            localFileSize = Files.size(path);
        } else {
            /* crea el archivo */
            Files.createFile(path);
        }

        /* Valida el tamaño del archivo local */
        if (localFileSize == connection.getContentLength()) {
            /*
             * Si el tamaño del archivo local y el archivo online son los mismo, retorna un
             * mensaje archivo ya descargado
             */
            log.warn("El archivo ya fue descargado");
            return "El archivo ya fue descargado";

        }

        /*
         * En caso de que el tamaño del archivo local sea diferente(menor) al del
         * archivo online, nuevamente abre la conexion
         */
        connection = url.openConnection();

        /* Agrega un rango de bytes desde el cual comezar a descargar */
        connection.setRequestProperty("Range", "bytes=" + localFileSize + "-");

        /* Iniciamos la descarga */
        downloaderService.downLoadFile(path, connection, localFileSize, downloadRequest);

        /* retorna un mensaje de que se ha iniciado la descarga */
        return "Descarga iniciada";
    }

}
