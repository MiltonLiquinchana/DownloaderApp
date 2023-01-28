package com.mflq.downloader.implement;

import com.mflq.downloader.service.ProgressCallBackService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ProgressCallBackServiceImpl implements ProgressCallBackService {


    @Override
    public void rbcProgressCallback(long sizeRead, double progress) {

        log.info("download progress {} Megabytes received, {}%", sizeRead, String.format("%.02f", progress));


    }
}
