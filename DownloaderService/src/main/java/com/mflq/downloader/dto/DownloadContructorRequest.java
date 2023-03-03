package com.mflq.downloader.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DownloadContructorRequest {

		private String url;

		private String downloadFileName;

		private BigDecimal downloadFileLength;

		private String downloadFileDescription;

		private Boolean downloadFilestatus;
		
		private Integer fileType;
		
		private Integer category;



}
