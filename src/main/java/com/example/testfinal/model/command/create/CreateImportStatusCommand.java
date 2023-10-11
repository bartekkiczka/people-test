package com.example.testfinal.model.command.create;

import com.example.testfinal.enums.UploadStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateImportStatusCommand {

    @Enumerated(EnumType.STRING)
    private UploadStatus status;
}
