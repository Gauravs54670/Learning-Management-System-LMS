package com.Gaurav.LMS3.Exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @Builder
public class APIError {
    private int responseStatus;
    private String message;
    private LocalDateTime timeStamp;
}
