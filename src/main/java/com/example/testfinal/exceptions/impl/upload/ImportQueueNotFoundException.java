package com.example.testfinal.exceptions.impl.upload;

public class ImportQueueNotFoundException extends RuntimeException{
    public ImportQueueNotFoundException(){
        super("Can't find import queue item");
    }
}
