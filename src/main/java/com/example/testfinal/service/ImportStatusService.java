package com.example.testfinal.service;

import com.example.testfinal.enums.UploadStatus;
import com.example.testfinal.model.ImportStatus;
import com.example.testfinal.repository.ImportStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ImportStatusService {

    private final ImportStatusRepository importStatusRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateImportStatus(long currentProcessedRows, ImportStatus importStatus, UploadStatus Status) {
        importStatus.setStatus(Status);
        importStatus.setProcessedRows(currentProcessedRows);
        importStatusRepository.save(importStatus);
    }
}
