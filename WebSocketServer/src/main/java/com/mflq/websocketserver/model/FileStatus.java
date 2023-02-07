package com.mflq.websocketserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileStatus {
    private String from;
    private String to;
    private long sizeRead;
    private double progress;
}
