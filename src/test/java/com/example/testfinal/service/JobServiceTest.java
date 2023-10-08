package com.example.testfinal.service;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.exceptions.impl.job.JobEndDateIsBeforeStartDateException;
import com.example.testfinal.exceptions.impl.job.JobNotFoundException;
import com.example.testfinal.model.Job;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JobServiceTest {

    @Autowired
    JobService jobService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setup() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    public void testFindAllShouldReturnJobs(){
        //given
        List<Job> jobs = jobService.findAll(PageRequest.of(0,10)).getContent();

        //when
        //then
        assertEquals(jobs.size(), 1);
    }

    @Test
    public void testFindByIdShouldFindJob(){
        //given
        //when
        Job job = jobService.findById(1);

        //then
        assertEquals(job.getId(), 1);
        assertEquals(job.getSalary(), 2500);
        assertEquals(job.getStartDate(), LocalDate.of(2020,1,1));
        assertEquals(job.getEndDate(), LocalDate.of(2021,1,1));
    }

    @Test
    public void testFindByIdShouldNotFindJobWithInvalidId(){
        //given
        //when
        //then
        assertThrows(JobNotFoundException.class, () -> jobService.findById(2));
    }

    @Test
    public void testSaveShouldSaveJob(){
        //given
        Job job = Job.builder()
                .name("test")
                .salary(1000L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();

        //when
        jobService.save(job);

        //then
        Job dbJob = jobService.findById(2);

        assertEquals(dbJob.getId(), 2);
        assertEquals(dbJob.getName(), job.getName());
        assertEquals(dbJob.getSalary(), job.getSalary());
        assertEquals(dbJob.getStartDate(), job.getStartDate());
        assertEquals(dbJob.getEndDate(), job.getEndDate());
    }

    @Test
    public void testSaveShouldNotSaveJobWhenEndDateIsBeforeStartDate(){
        //given
        Job job = Job.builder()
                .name("test")
                .salary(1000L)
                .startDate(LocalDate.of(2020,1,1))
                .endDate(LocalDate.of(2019,1,1))
                .build();

        //when
        //then
        assertThrows(JobEndDateIsBeforeStartDateException.class, () -> jobService.save(job));
    }

    @Test
    public void testDeleteShouldDeleteJob(){
        //given
        assertEquals(1, jobService.findAll(PageRequest.of(0 ,10)).getContent().size());

        //when
        jobService.deleteById(1);

        //then
        assertEquals(0, jobService.findAll(PageRequest.of(0 ,10)).getContent().size());
    }

    @Test
    public void testDeleteShouldNotDeleteNotExistingJob(){
        //given
        assertEquals(1, jobService.findAll(PageRequest.of(0 ,10)).getContent().size());

        //when
        jobService.deleteById(100);

        //then
        assertEquals(1, jobService.findAll(PageRequest.of(0 ,10)).getContent().size());
    }

}