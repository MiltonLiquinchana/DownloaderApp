package com.mflq.downloader.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadContructorRequest {
	private String url;
	private String fileName;
	private String fileOutputPath;

}
