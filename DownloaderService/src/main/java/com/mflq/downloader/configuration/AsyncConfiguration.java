package com.mflq.downloader.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {
    @Bean(name = "asyncExecutor")
    Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        /*Minimo de hilos que se va a utilizar en la app*/
        executor.setCorePoolSize(3);

        /*Maximo de hilos que se van a crear despues de que todos los hilos(setcorepoolsize) se esten utilizando*/
        executor.setMaxPoolSize(6);

        /*El maximo de hilos que pueden ser creados en total, pero que estan a la espera para ser atendidos,
         * digamos ya se llego a utilizar todos los hilos anteriormente definidos, entoces estos hilos de aqui van
         * a estar a la espera de que alguno se libere*/
        executor.setQueueCapacity(500);

        /*Nombre que se le puede colocar al hilo que se esta creando*/
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}
