package com.mflq.downloader.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadPartFile {
	private String filePartName;
	private Integer startPointBytes;
	private Integer endPointBytes;
	

}
