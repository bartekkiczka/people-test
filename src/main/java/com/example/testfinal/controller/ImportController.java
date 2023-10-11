package com.example.testfinal.controller;

import com.example.testfinal.enums.UploadStatus;
import com.example.testfinal.exceptions.impl.upload.ImportStatusNotFoundException;
import com.example.testfinal.model.ImportQueueItem;
import com.example.testfinal.model.ImportStatus;
import com.example.testfinal.model.dto.ImportStatusDto;
import com.example.testfinal.responses.ImportResponse;
import com.example.testfinal.service.ImportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class ImportController {

    private final ImportService importService;
    private final ModelMapper modelMapper;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${rabbitmq.import.queue}")
    private String importQueue;

    @GetMapping("/statuses")
    public ResponseEntity<List<ImportStatusDto>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return new ResponseEntity<>(importService.findAll(pageable).stream()
                .map(importStatus -> modelMapper.map(importStatus, ImportStatusDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ImportResponse> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        ImportResponse response = ImportResponse.builder()
                .message("Import started")
                .build();

        importService.getLastImportStatus().ifPresent(importStatus -> {
            if (importStatus.getStatus() == UploadStatus.STARTED) {
                response.setMessage("Another import is currently Running. Your request has been added to the queue");
            }
        });

        File tempFile = File.createTempFile("temp-csv-", ".csv");

        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImportQueueItem item = new ImportQueueItem(tempFile);
        String importQueueJson = objectMapper.writeValueAsString(item);
        rabbitTemplate.convertAndSend(importQueue, importQueueJson);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<ImportStatusDto> getImportStatus() {
        ImportStatus status = importService.getLastImportStatus().orElseThrow(ImportStatusNotFoundException::new);
        return new ResponseEntity<>(modelMapper.map(status, ImportStatusDto.class), HttpStatus.OK);
    }
}
