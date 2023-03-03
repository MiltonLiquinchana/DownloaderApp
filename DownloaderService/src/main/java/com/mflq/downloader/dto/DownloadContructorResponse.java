package com.mflq.downloader.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DownloadContructorResponse {

	private String url;

	private String downloadFileName;

	private BigDecimal downloadFileLength;

	private String downloadFileDescription;

	private Boolean downloadFilestatus;

	private String fileType;

	private String category;
	
	private String categoryFileOutputPath;
	
	private List<DownloadPartFile> downloadPartFiles;

}
