package com.mflq.downloader.model;

import org.springframework.data.rest.core.config.Projection;

//@Projection(name = "noAddresses", types = { DownloadFile.class })
public interface DownloadFileProjection {
//	@Value("#{target.dfName +''+target.cName}")
	String getDfName();
}
