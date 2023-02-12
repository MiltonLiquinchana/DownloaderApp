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

@Log4j2
@RestController
@RequestMapping("downloader")
public class DownloaderController {
	@Autowired
	private DownloaderService downloaderService;

	@PostMapping("download")
	public String downloadStart(@RequestBody DownloadRequest downloadRequest) throws IOException, URISyntaxException {
		URL url = new URL(downloadRequest.getUrlRequest());
		URLConnection connection = url.openConnection();
		Path path = Path.of(downloadRequest.getFileOutputPath(), downloadRequest.getFileName());
		long localFileSize = 0;

		if (Files.exists(path)) {
			localFileSize = Files.size(path);
		} else {
			Files.createFile(path);
		}

		if (localFileSize == connection.getContentLength()) {
			log.warn("El archivo ya fue descargado");
			return "El archivo ya fue descargado";

		}

		connection = url.openConnection();
		connection.setRequestProperty("Range", "bytes=" + localFileSize + "-");
		downloaderService.downLoadFile(path, connection, localFileSize, downloadRequest);
		return "Descarga iniciada";
	}

	@GetMapping("contructDownload")
	public ResponseEntity<DownloadContructorResponse> constructDownload(
			@RequestBody DownloadContructorRequest downloadContructorRequest) {
		
		
		return new ResponseEntity<DownloadContructorResponse>(downloaderService.downloadContructor(downloadContructorRequest), HttpStatus.OK);
	}

}
