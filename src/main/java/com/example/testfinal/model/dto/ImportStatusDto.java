package com.example.testfinal.model.dto;

import com.example.testfinal.enums.UploadStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportStatusDto {

    private long id;

    private UploadStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long processedRows;
}
