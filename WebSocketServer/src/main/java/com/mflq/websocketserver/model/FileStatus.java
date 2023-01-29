package com.mflq.websocketserver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileStatus {
    private long sizeRead;
    private double progress;
}
