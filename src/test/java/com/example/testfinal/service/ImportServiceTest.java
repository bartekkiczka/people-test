package com.example.testfinal.service;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.enums.UploadStatus;
import com.example.testfinal.exceptions.impl.upload.ImportStatusNotFoundException;
import com.example.testfinal.model.ImportStatus;
import com.example.testfinal.repository.ImportStatusRepository;
import com.example.testfinal.repository.PersonRepository;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;

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

        //when
        importService.uploadFile(tempFile, 10);
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
        importService.uploadFile(tempFile, 10);
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


        importService.uploadFile(tempFile, 10);
        Thread.sleep(500);

        ImportStatus currentImportStatus = importStatusRepository.findTopByOrderByIdDesc().orElseThrow(ImportStatusNotFoundException::new);

        assertEquals(currentImportStatus.getStatus(), UploadStatus.COMPLETED);
        assertNotNull(currentImportStatus.getStartDate());
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

    private File createTemporaryCsvFileWithValidData2() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("person_type,name,surname,pesel,height,weight,email,employment_start_date,position,salary,school_name,year_of_study,field_of_study,scholarship,pension,years_worked\n");
            for (int i = 0; i < 100000; i++) {
                writer.write("STUDENT,name,surname," + (10000000000L + i) + ",10,10,student" + (1 + i) + "@test.com,null,null,null,school,2,field,100,null,null\n");
            }
        }


        return tempFile;
    }
}