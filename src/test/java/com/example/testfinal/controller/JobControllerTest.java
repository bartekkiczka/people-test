package com.example.testfinal.controller;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Job;
import com.example.testfinal.service.JobService;
import com.example.testfinal.service.PersonService;
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

import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class JobControllerTest {

    private final String ADMIN_ROLE_VALUE = "Basic YWRtaW46YWRtaW4=";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private JobService jobService;

    @BeforeEach
    public void setup() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    public void testGetJobsShouldReturnJobs() throws Exception {
        mvc.perform(get("/jobs"))
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
        mvc.perform(get("/jobs/1"))
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
        mvc.perform(get("/jobs/10"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Can't find job with id: 10"))
                .andExpect(jsonPath("$.description").value("uri=/jobs/10"));
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

        mvc.perform(post("/jobs")
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

        mvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Job end date is before start date"))
                .andExpect(jsonPath("$.description").value("uri=/jobs"));
    }

    @Test
    public void testDeleteShouldDeleteJob() throws Exception{
        mvc.perform(get("/jobs/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.salary").value(2500))
                .andExpect(jsonPath("$.startDate").value("2020-01-01"))
                .andExpect(jsonPath("$.endDate").value("2021-01-01"))
                .andExpect(jsonPath("$.employeeId").value(0));

        mvc.perform(delete("/jobs/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(get("/jobs/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Can't find job with id: 1"))
                .andExpect(jsonPath("$.description").value("uri=/jobs/1"));
    }

    @Test
    public void testGetEmployeeJobsShouldReturnJobs() throws Exception {
        jobService.addEmployeeJob(2, 1);

        mvc.perform(get("/jobs/employee/2/jobs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("name1"))
                .andExpect(jsonPath("$.[0].salary").value(2500))
                .andExpect(jsonPath("$.[0].startDate").value("2020-01-01"))
                .andExpect(jsonPath("$.[0].endDate").value("2021-01-01"))
                .andExpect(jsonPath("$.[0].employeeId").value(2));
    }


    @Test
    public void testGetEmployeeJobsShouldThrowPersonIsNotEmployeeException() throws Exception {
        mvc.perform(get("/jobs/employee/1/jobs"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Person with id 1 is not an Employee"))
                .andExpect(jsonPath("$.description").value("uri=/jobs/employee/1/jobs"));
    }

    @Test
    public void testGetEmployeeJobsShouldThrowPersonNotExistsException() throws Exception {
        mvc.perform(get("/jobs/employee/999/jobs"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Can't find person with id: 999"))
                .andExpect(jsonPath("$.description").value("uri=/jobs/employee/999/jobs"));
    }


    @Test
    public void testAddEmployeeJobShouldAddJob() throws Exception {
        //given
        mvc.perform(patch("/jobs/1/employee/2")
                        .header("Authorization", ADMIN_ROLE_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //when
        //then
        mvc.perform(get("/jobs/employee/2/jobs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("name1"))
                .andExpect(jsonPath("$.[0].salary").value(2500))
                .andExpect(jsonPath("$.[0].startDate").value("2020-01-01"))
                .andExpect(jsonPath("$.[0].endDate").value("2021-01-01"))
                .andExpect(jsonPath("$.[0].employeeId").value(2));
    }

    @Test
    public void testAddEmployeeJobShouldThrowJobDatesOverlapException() throws Exception {
        //given
        Job job = Job.builder()
                .name("job2")
                .salary(1000L)
                .startDate(LocalDate.of(2020, 2, 2))
                .endDate(LocalDate.of(2021, 2, 2))
                .build();
        jobService.save(job);

        jobService.addEmployeeJob(2, 1);

        //when
        //then
        mvc.perform(patch("/jobs/2/employee/2")
                        .header("Authorization", ADMIN_ROLE_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Position dates overlap with existing positions"))
                .andExpect(jsonPath("$.description").value("uri=/jobs/2/employee/2"))
                .andReturn();
    }

    @Test
    public void testAddEmployeeJobShouldThrowPersonIsNotEmployeeException() throws Exception {
        mvc.perform(patch("/jobs/1/employee/1")
                        .header("Authorization", ADMIN_ROLE_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Person with id 1 is not an Employee"))
                .andExpect(jsonPath("$.description").value("uri=/jobs/1/employee/1"))
                .andReturn();
    }

    @Test
    public void testAddEmployeeJobShouldThrowJobAlreadyAssignedException() throws Exception {
        jobService.addEmployeeJob(2, 1);

        mvc.perform(patch("/jobs/1/employee/2")
                        .header("Authorization", ADMIN_ROLE_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("This job is already assigned"))
                .andExpect(jsonPath("$.description").value("uri=/jobs/1/employee/2"))
                .andReturn();
    }

    @Test
    public void testRemoveEmployeeJobShouldRemoveJob() throws Exception {
        //given
        jobService.addEmployeeJob(2, 1);

        //when
        mvc.perform(patch("/jobs/1/employee/2/remove"))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        mvc.perform(get("/jobs/employee/2/jobs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()));
    }

    @Test
    public void testRemoveEmployeeJobShouldThrowException() throws Exception {
        //given
        //when
        //then
        mvc.perform(patch("/jobs/1/employee/2/remove"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Employee with id: 2 does not contain job with id: 1"))
                .andExpect(jsonPath("$.description").value("uri=/jobs/1/employee/2/remove"));
    }

}