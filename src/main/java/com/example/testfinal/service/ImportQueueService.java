package com.example.testfinal.service;

import com.example.testfinal.model.ImportQueueItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImportQueueService {

    private final ImportService importService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "import-queue")
    public void processImport(String importQueueJson) {
        try {
            ImportQueueItem item = objectMapper.readValue(importQueueJson, ImportQueueItem.class);
            importService.uploadFile(item.getFile(), item.getStatusId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
