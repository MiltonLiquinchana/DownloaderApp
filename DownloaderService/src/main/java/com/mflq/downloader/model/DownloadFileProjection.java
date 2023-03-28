package com.mflq.downloader.model;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;

public interface DownloadFileProjection {

	@Value("#{target.DF_URL}")
	String getDownloadFileUrl();

	@Value("#{target.DF_Name}")
	String getDownloadFileName();

	@Value("#{target.DF_FileLength}")
	BigDecimal getDownloadFileLength();

	@Value("#{target.DF_STATUS}")
	boolean getDownloadFilestatus();

	@Value("#{target.DF_Description}")
	String getDownloadFileDescription();

	@Value("#{target.FT_Name}")
	String getFileTypeName();

	@Value("#{target.C_Name}")
	String getCategoryName();

	@Value("#{target.C_FileOutputPath}")
	String getCategoryFileOutputPath();

}
