package com.mflq.downloader.service;

import org.springframework.scheduling.annotation.Async;

public interface ProgressCallBackService {
    // El delegado RBC Wrapper recibe mensajes rbc ProgressCallback() del ciclo de lectura.
    // Se pasa el progreso como un porcentaje si se conoce, o -1.0 para indicar un progreso indeterminado.
    //
    //Esta devolución de llamada bloquea el ciclo de lectura,
    // por lo que una implementación inteligente pasará la menor cantidad de tiempo posible aquí antes de regresar.
    //
    // Una posible implementación es enviar el mensaje de progreso autómicamente a una cola administrada por un subproceso
    // secundario y luego activar ese subproceso.
    // El subproceso del administrador de colas luego actualiza la barra de progreso de la interfaz de usuario.
    // Esto permite que el bucle de lectura continúe lo más rápido posible.
    @Async("asyncExecutor")
    void rbcProgressCallback(long sizeRead, double progress);
}