package com.example.testfinal.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ErrorMessage {
    private int statusCode;
    private LocalDateTime timeStamp;
    private String message;
    private String description;
}