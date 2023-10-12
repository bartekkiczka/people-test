package com.example.testfinal.service;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.enums.UploadStatus;
import com.example.testfinal.factory.person.PersonFactory;
import com.example.testfinal.model.ImportStatus;
import com.example.testfinal.repository.ImportStatusRepository;
import com.example.testfinal.repository.PersonRepository;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("test")
class ImportServiceTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportStatusRepository importStatusRepository;

    @Autowired
    private ImportService importService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ImportStatusService importStatusService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PersonFactory personFactory;

    @BeforeEach
    public void setup() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    public void testUploadFileShouldUploadFile() throws Exception {
        //given
        File csvFile = createTemporaryCsvFileWithValidData();
        MockMultipartFile file = new MockMultipartFile("file", csvFile.getName(), "text/csv",
                new FileInputStream(csvFile));
        File tempFile = createTempFile(file);

        assertEquals(3, personRepository.findAll(PageRequest.of(0, 10)).getContent().size());

        ImportStatus importStatus = ImportStatus.builder()
                .status(UploadStatus.QUEUED)
                .createdDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .processedRows(null)
                .build();
        importStatusRepository.save(importStatus);

        //when
        importService.uploadFile(tempFile, 1);
        Thread.sleep(500);

        //then
        assertEquals(4, personRepository.findAll(PageRequest.of(0, 10)).getContent().size());
    }

    @Test
    public void testUploadFileShouldNotUploadInvalidData() throws Exception {
        //given
        File csvFile = createTemporaryCsvFileWithInvalidData();
        MockMultipartFile file = new MockMultipartFile("file", csvFile.getName(), "text/csv",
                new FileInputStream(csvFile));
        File tempFile = createTempFile(file);

        assertEquals(3, personRepository.findAll(PageRequest.of(0, 10)).getContent().size());

        //when
        importService.uploadFile(tempFile, 1);
        Thread.sleep(500);

        //then
        assertEquals(3, personRepository.findAll(PageRequest.of(0, 10)).getContent().size());
    }

    @Test
    public void testUploadShouldReturnCompletedImportStatus() throws Exception {
        //given
        File csvFile = createTemporaryCsvFileWithValidData();
        MockMultipartFile file = new MockMultipartFile("file", csvFile.getName(), "text/csv",
                new FileInputStream(csvFile));
        File tempFile = createTempFile(file);

        Instant fixedInstant = Instant.parse("2023-10-12T12:00:00Z");
        ZoneId zoneId = ZoneId.of("UTC+2");
        Clock fixedClock = Clock.fixed(fixedInstant, zoneId);

        ImportService tempImportService = new ImportService(
                importStatusRepository,
                importStatusService,
                jdbcTemplate,
                personFactory,
                modelMapper,
                fixedClock
        );

        ImportStatus importStatus = ImportStatus.builder()
                .status(UploadStatus.QUEUED)
                .createdDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .processedRows(null)
                .build();
        importStatusRepository.save(importStatus);

        //when
        tempImportService.uploadFile(tempFile, 1);
        Thread.sleep(500);

        ImportStatus currentImportStatus = importService.findById(1);

        LocalDateTime clockDateTime = LocalDateTime.ofInstant(fixedClock.instant(), fixedClock.getZone());

        //then
        assertEquals(currentImportStatus.getStatus(), UploadStatus.COMPLETED);
        assertNotNull(currentImportStatus.getCreatedDate());
        assertEquals(currentImportStatus.getStartDate(), clockDateTime);
        assertNotNull(currentImportStatus.getEndDate());
    }
    private File createTempFile(MockMultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp-csv-", ".csv");
        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    private File createTemporaryCsvFileWithValidData() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("person_type,name,surname,pesel,height,weight,email,employment_start_date,position,salary,school_name,year_of_study,field_of_study,scholarship,pension,years_worked\n");
            writer.write("STUDENT,name,surname,63195044816,10,10,student711@test.com,null,null,null,school,2,field,100,null,null\n");
        }

        return tempFile;
    }

    private File createTemporaryCsvFileWithInvalidData() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("person_type,name,surname,pesel,height,weight,email,employment_start_date,position,salary,school_name,year_of_study,field_of_study,scholarship,pension,years_worked\n");
            writer.write("STUDENTT,name,surname,63195044816,10,10,email1@test.com,null,null,null,school,2,field,100,null,null\n");
        }

        return tempFile;
    }
}