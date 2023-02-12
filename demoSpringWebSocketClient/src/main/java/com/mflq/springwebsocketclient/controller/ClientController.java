package com.mflq.springwebsocketclient.controller;

import com.mflq.springwebsocketclient.service.GeneratorClientService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientController {
    @Autowired
    private GeneratorClientService generatorClientService;

    @PostConstruct
    public void init() {
        for (int i = 0; i < 2; i++) {
            generatorClientService.generateClient("userClient" + i);
        }


    }
}
