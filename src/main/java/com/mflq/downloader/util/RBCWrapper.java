package com.mflq.downloader.util;

import com.mflq.downloader.service.DownloaderService;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

@Log4j2
public class RBCWrapper implements ReadableByteChannel {
    DownloaderService delegate;
    private final long sizeFileOnline;
    private final ReadableByteChannel readableByteChannel;
    private long sizeRead;


    public RBCWrapper(ReadableByteChannel readableByteChannel, long sizeFileLocal, long sizeFileOnline, DownloaderService delegate) {
        this.readableByteChannel = readableByteChannel;
        this.sizeRead = sizeFileLocal;
        this.sizeFileOnline = sizeFileOnline;
        this.delegate = delegate;
    }

    @Override
    public void close() throws IOException {
        this.readableByteChannel.close();
    }


    @Override
    public boolean isOpen() {
        return this.readableByteChannel.isOpen();
    }

    @Override
    public int read(ByteBuffer bb) throws IOException {
        int n;
        if ((n = this.readableByteChannel.read(bb)) > 0) {

            this.sizeRead += n;

            double progress = this.sizeFileOnline > 0 ? (((double) this.sizeRead / (double) this.sizeFileOnline) * 100.0) : -1.0;
            this.delegate.llamada(this.sizeRead, progress);
//            this.progressCallBackService.rbcProgressCallback(this.sizeRead, progress);
//            System.out.println("******************************************************************************");
        }
        return n;
    }

  /*  public long getReadSoFar() {
        return this.sizeRead;
    }*/


}