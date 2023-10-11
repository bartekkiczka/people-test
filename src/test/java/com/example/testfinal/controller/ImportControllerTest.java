package com.example.testfinal.controller;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.enums.UploadStatus;
import com.example.testfinal.model.ImportStatus;
import com.example.testfinal.repository.ImportStatusRepository;
import com.example.testfinal.service.ImportService;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.*;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ImportControllerTest {

    private final String ADMIN_ROLE_VALUE = "Basic YWRtaW46YWRtaW4=";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ImportStatusRepository importStatusRepository;

    @Autowired
    private ImportService importService;

    @BeforeEach
    public void setup() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    public void testFindAllShouldFindAllImportStatuses() throws Exception {
        //given
        ImportStatus importStatus = ImportStatus.builder()
                .status(UploadStatus.STARTED)
                .processedRows(100L)
                .build();

        //when
        importStatusRepository.save(importStatus);

        //then
        mvc.perform(get("/upload/statuses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].startDate").isNotEmpty())
                .andExpect(jsonPath("$.[0].endDate").isNotEmpty())
                .andExpect(jsonPath("$.[0].processedRows").value(100L));
    }

    @Test
    public void testUploadFileShouldReturnOkStatus() throws Exception {
        //given
        File csvFile = createTemporaryCsvFileWithValidData();
        MockMultipartFile file = new MockMultipartFile("file", csvFile.getName(), "text/csv",
                new FileInputStream(csvFile));

        //when
        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .param("timeout", "1")
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ROLE_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadFileShouldInformThatOtherImportIsRunning() throws Exception {
        //given
        File csvFile = createTemporaryCsvFileWithValidData();
        MockMultipartFile file = new MockMultipartFile("file", csvFile.getName(), "text/csv",
                new FileInputStream(csvFile));

        //when
        ImportStatus importStatus = ImportStatus.builder()
                .status(UploadStatus.STARTED)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .processedRows(100L)
                .build();
        importStatusRepository.save(importStatus);

        //then
        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .param("timeout", "10")
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ROLE_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Another import is currently Running. Your request has been added to the queue"));
    }

    @Test
    public void testUploadShouldNotUploadWithoutAuthorization() throws Exception {
        //given
        File csvFile = createTemporaryCsvFileWithValidData();
        MockMultipartFile file = new MockMultipartFile("file", csvFile.getName(), "text/csv",
                new FileInputStream(csvFile));

        //when
        //then
        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetImportStatusShouldReturnLastStatus() throws Exception {
        //given
        ImportStatus status = ImportStatus.builder()
                .status(UploadStatus.STARTED)
                .processedRows(100L)
                .build();

        //when
        importStatusRepository.save(status);

        //then
        mvc.perform(get("/upload/status"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(status.getStatus().toString()))
                .andExpect(jsonPath("$.startDate").isNotEmpty())
                .andExpect(jsonPath("$.endDate").isNotEmpty())
                .andExpect(jsonPath("$.processedRows").value(status.getProcessedRows()));
    }

    @Test
    public void testGetStatusShouldThrowImportStatusNotFoundException() throws Exception {
        mvc.perform(get("/upload/status"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Import status not found"))
                .andExpect(jsonPath("$.description").value("uri=/upload/status"));
    }

    private File createTemporaryCsvFileWithValidData() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("person_type,name,surname,pesel,height,weight,email,employment_start_date,position,salary,school_name,year_of_study,field_of_study,scholarship,pension,years_worked\n");
            writer.write("STUDENT,name,surname,63195047816,10,10,student@test.com,null,null,null,school,2,field,100,null,null\n");
        }

        return tempFile;
    }

}