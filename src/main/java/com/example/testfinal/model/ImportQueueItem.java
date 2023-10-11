package com.example.testfinal.model;

import lombok.*;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportQueueItem {
    private File file;
}
