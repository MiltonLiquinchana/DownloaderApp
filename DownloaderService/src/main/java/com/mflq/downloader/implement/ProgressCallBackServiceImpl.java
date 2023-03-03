package com.mflq.downloader.implement;

import org.springframework.stereotype.Service;

import com.mflq.downloader.service.ProgressCallBackService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ProgressCallBackServiceImpl implements ProgressCallBackService {

	@Override
	public void rbcProgressCallback(long sizeRead, double progress) {

		log.debug("download progress {} Megabytes received, {}%", sizeRead, String.format("%.02f", progress));

	}
}
