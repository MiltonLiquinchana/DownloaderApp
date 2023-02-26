package com.mflq.downloader.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadPartFile {
	private String filePartName;
	private Integer startPointBytes;
	private Integer endPointBytes;
	

}
