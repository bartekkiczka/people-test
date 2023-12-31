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
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
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
                .status(UploadStatus.QUEUED)
                .createdDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .processedRows(100L)
                .build();

        //when
        importStatusRepository.save(importStatus);

        //then
        mvc.perform(get("/upload/statuses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].createdDate").isNotEmpty())
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
                .createdDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .processedRows(100L)
                .build();
        importStatusRepository.save(importStatus);

        //then
        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ROLE_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Another import is currently Running. Your request with id 2 has been added to the queue"));
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
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
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

    @Test
    public void testGetStatusByIdShouldReturnStatus() throws Exception{
        Instant fixedInstant = Instant.parse("2023-10-12T12:00:00Z");
        ZoneId zoneId = ZoneId.of("UTC+2");
        Clock fixedClock = Clock.fixed(fixedInstant, zoneId);

        ImportStatus importStatus = ImportStatus.builder()
                .status(UploadStatus.QUEUED)
                .startDate(LocalDateTime.now(fixedClock))
                .processedRows(100L)
                .build();
        importStatusRepository.save(importStatus);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expectedStartDateFormatted = importStatus.getStartDate().format(formatter);

        mvc.perform(get("/upload/status/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(importStatus.getStatus().toString()))
                .andExpect(jsonPath("$.startDate").value(expectedStartDateFormatted))
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
                .andExpect(jsonPath("$.endDate").isNotEmpty())
                .andExpect(jsonPath("$.processedRows").value(importStatus.getProcessedRows()));
    }

    @Test
    public void testGetStatusByIdShouldThrowStatusNotFoundException() throws Exception{
        mvc.perform(get("/upload/status/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Import status not found"))
                .andExpect(jsonPath("$.description").value("uri=/upload/status/999"));
    }

    @Test
    public void testUploadShouldQueueAndUploadAllRequests() throws Exception{
        //given
        File csvFile = createTemporaryCsvFileWithValidData();
        MockMultipartFile file = new MockMultipartFile("file", csvFile.getName(), "text/csv",
                new FileInputStream(csvFile));

        File csvFile2 = createTemporaryCsvFileWithValidData2();
        MockMultipartFile file2 = new MockMultipartFile("file", csvFile.getName(), "text/csv",
                new FileInputStream(csvFile2));

        //when
        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ROLE_VALUE))
                .andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file2)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_ROLE_VALUE))
                .andExpect(status().isOk());
        Thread.sleep(500);

        //then
        mvc.perform(get("/upload/status/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        mvc.perform(get("/upload/status/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        mvc.perform(get("/people"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    private File createTemporaryCsvFileWithValidData() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("person_type,name,surname,pesel,height,weight,email,employment_start_date,position,salary,school_name,year_of_study,field_of_study,scholarship,pension,years_worked\n");
            writer.write("STUDENT,name,surname,63195047816,10,10,student@test.com,null,null,null,school,2,field,100,null,null\n");
        }

        return tempFile;
    }

    private File createTemporaryCsvFileWithValidData2() throws IOException {
        File tempFile = File.createTempFile("test2", ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("person_type,name,surname,pesel,height,weight,email,employment_start_date,position,salary,school_name,year_of_study,field_of_study,scholarship,pension,years_worked\n");
            writer.write("STUDENT,name,surname,63194047816,10,10,student1@test.com,null,null,null,school,2,field,100,null,null\n");
        }

        return tempFile;
    }

}