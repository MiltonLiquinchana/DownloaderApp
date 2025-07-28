package com.mflq.downloader.util;

import com.mflq.downloader.dto.DownloadRangeRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DividirBytes {

    /**
     * Divide un total de bytes en partes casi iguales.
     * Si no se puede dividir exactamente, la última parte recibe el sobrante.
     *
     * @param totalBytes Cantidad total de bytes a dividir.
     * @param numPartes  Número de partes en las que se quiere dividir.
     * @return Un arreglo con los tamaños de cada parte.
     */
    public List<DownloadRangeRequest> dividirBytes(int totalBytes, int numPartes) {
        // Calcula la cantidad base de bytes por cada parte (división entera)
        int base = totalBytes / numPartes;

        // Crea un arreglo para guardar el resultado
        List<DownloadRangeRequest> partes = new ArrayList<>();

        // Asigna la parte base a las primeras n - 1 posiciones
        for (int i = 1; i <= numPartes; i++) {
            DownloadRangeRequest downloadRangeRequest= new DownloadRangeRequest();
            downloadRangeRequest.setStartByte(((long) base *i)-base);
            downloadRangeRequest.setEndByte( ((long) base *i)-1);
            if(i==numPartes){
                downloadRangeRequest.setEndByte(totalBytes-1);
            }
            partes.add(downloadRangeRequest);
        }
        return partes;
    }

}
