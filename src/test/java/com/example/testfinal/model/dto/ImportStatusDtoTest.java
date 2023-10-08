package com.example.testfinal.model.dto;

import com.example.testfinal.enums.UploadStatus;
import com.example.testfinal.model.ImportStatus;
import com.example.testfinal.model.dto.ImportStatusDto;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ImportStatusDtoTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testImportStatusToImportStatusDtoConversion(){
        ImportStatus importStatus = ImportStatus.builder()
                .status(UploadStatus.COMPLETED)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .processedRows(1000L)
                .timeout(10)
                .build();

        ImportStatusDto importStatusDto = modelMapper.map(importStatus, ImportStatusDto.class);

        assertEquals(importStatusDto.getStatus(), importStatus.getStatus());
        assertEquals(importStatusDto.getStartDate(), importStatus.getStartDate());
        assertEquals(importStatusDto.getEndDate(), importStatus.getEndDate());
        assertEquals(importStatusDto.getProcessedRows(), importStatus.getProcessedRows());
        assertEquals(importStatusDto.getTimeout(), importStatus.getTimeout());
    }
}