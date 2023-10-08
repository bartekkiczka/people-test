package com.example.testfinal.controller;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.model.Job;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class JobControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    public void testGetJobsShouldReturnJobs() throws Exception {
        mvc.perform(get("/job"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("name1"))
                .andExpect(jsonPath("$.[0].salary").value(2500))
                .andExpect(jsonPath("$.[0].startDate").value("2020-01-01"))
                .andExpect(jsonPath("$.[0].endDate").value("2021-01-01"));
    }

    @Test
    public void testFindByIdShouldReturnJob() throws Exception {
        mvc.perform(get("/job/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.salary").value(2500))
                .andExpect(jsonPath("$.startDate").value("2020-01-01"))
                .andExpect(jsonPath("$.endDate").value("2021-01-01"));
    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenIdIsInvalid() throws Exception {
        mvc.perform(get("/job/10"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Can't find job with id: 10"))
                .andExpect(jsonPath("$.description").value("uri=/job/10"));
    }

    @Test
    public void testSaveShouldSaveJob() throws Exception {
        Job job = Job.builder()
                .name("test")
                .salary(1000L)
                .startDate(LocalDate.of(2022, 1, 1))
                .endDate(LocalDate.of(2023, 1, 1))
                .build();

        String jobJson = objectMapper.writeValueAsString(job);

        mvc.perform(post("/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.salary").value(1000))
                .andExpect(jsonPath("$.startDate").value("2022-01-01"))
                .andExpect(jsonPath("$.endDate").value("2023-01-01"));
    }

    @Test
    public void testSaveJobShouldNotSaveJobWithEndDateBeforeStartDate() throws Exception {
        Job job = Job.builder()
                .name("test")
                .salary(1000L)
                .startDate(LocalDate.of(2022, 1, 1))
                .endDate(LocalDate.of(2021, 1, 1))
                .build();

        String jobJson = objectMapper.writeValueAsString(job);

        mvc.perform(post("/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Job end date is before start date"))
                .andExpect(jsonPath("$.description").value("uri=/job"));
    }

    @Test
    public void testDeleteShouldDeleteJob() throws Exception{
        mvc.perform(get("/job/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.salary").value(2500))
                .andExpect(jsonPath("$.startDate").value("2020-01-01"))
                .andExpect(jsonPath("$.endDate").value("2021-01-01"))
                .andExpect(jsonPath("$.employeeId").value(0));

        mvc.perform(delete("/job/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(get("/job/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Can't find job with id: 1"))
                .andExpect(jsonPath("$.description").value("uri=/job/1"));
    }

}