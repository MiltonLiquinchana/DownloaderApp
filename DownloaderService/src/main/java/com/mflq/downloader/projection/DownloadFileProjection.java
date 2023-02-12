package com.mflq.downloader.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.mflq.downloader.model.DownloadFile;
//@Projection(name ="downloadFileProjection", types = {DownloadFile.class})
public interface DownloadFileProjection {
//	@Value("#{target.dfName}")
	String getDfName();
}
