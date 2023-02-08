package com.mflq.downloader.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import com.mflq.downloader.handler.MyStompSessionHandler;
import com.mflq.downloader.model.DownloadRequest;
import com.mflq.downloader.service.DownloaderService;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RBCWrapper implements ReadableByteChannel {
    DownloaderService delegate;
    private final long sizeFileOnline;
    private final ReadableByteChannel readableByteChannel;
    private long sizeRead;
    private MyStompSessionHandler mysSessionHandler;
    private DownloadRequest downloadRequest;

    public RBCWrapper(ReadableByteChannel readableByteChannel, long sizeFileLocal, long sizeFileOnline, DownloaderService delegate,MyStompSessionHandler mysSessionHandler,DownloadRequest downloadRequest ) {
        this.readableByteChannel = readableByteChannel;
        this.sizeRead = sizeFileLocal;
        this.sizeFileOnline = sizeFileOnline;
        this.delegate = delegate;
        this.mysSessionHandler=mysSessionHandler;
        this.downloadRequest=downloadRequest;
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

            this.delegate.notifyDownloadProgres(this.sizeRead, progress,this.mysSessionHandler,downloadRequest	);

        }
        return n;
    }


}