package com.example.testfinal.controller;

import com.example.testfinal.enums.UploadStatus;
import com.example.testfinal.exceptions.impl.upload.ImportStatusNotFoundException;
import com.example.testfinal.exceptions.impl.upload.OtherImportRunningException;
import com.example.testfinal.model.ImportStatus;
import com.example.testfinal.model.dto.ImportStatusDto;
import com.example.testfinal.repository.ImportStatusRepository;
import com.example.testfinal.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class ImportController {

    private final ImportService importService;
    private final ImportStatusRepository importStatusRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/statuses")
    public ResponseEntity<List<ImportStatusDto>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return new ResponseEntity<>(importService.findAll(pageable).stream()
                .map(importStatus -> modelMapper.map(importStatus, ImportStatusDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("timeout") Integer timeout) throws Exception {

        importStatusRepository.findTopByOrderByIdDesc().ifPresent(importStatus -> {
            LocalDateTime timeoutDate = importStatus.getStartDate().plusSeconds(importStatus.getTimeout());
            if (importStatus.getStatus() == UploadStatus.STARTED && LocalDateTime.now().isBefore(timeoutDate)) {
                throw new OtherImportRunningException();
            }
        });

        File tempFile = File.createTempFile("temp-csv-", ".csv");

        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        importService.uploadFile(tempFile, timeout);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<ImportStatusDto> getImportStatus() {
        ImportStatus status = importStatusRepository.findTopByOrderByIdDesc()
                .orElseThrow(ImportStatusNotFoundException::new);
        return new ResponseEntity<>(modelMapper.map(status, ImportStatusDto.class), HttpStatus.OK);
    }
}
