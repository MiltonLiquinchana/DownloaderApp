package com.mflq.downloader.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DownloadContructorResponse {
	private String url;
	private String fileName;
	private List<DownloadPartFile> downloadPartFile;
	private Integer totalContendLength;
	private String fileOutputPath;
	private String contentType;
	private String category;
	

}
