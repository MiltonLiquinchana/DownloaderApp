package com.mflq.springwebsocketclient.service;

import org.springframework.scheduling.annotation.Async;

public interface GeneratorClientService {
    @Async("asyncExecutor")
    void generateClient(String userSubscribeName);

}
