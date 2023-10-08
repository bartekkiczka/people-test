package com.example.testfinal.model.dto;

import com.example.testfinal.model.Job;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JobDtoTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testJobToJobDtoConversion(){
        Job job = Job.builder()
                .name("name")
                .salary(2000L)
                .startDate(LocalDate.of(2000,1,1))
                .endDate(LocalDate.of(2001,1,1))
                .build();

        JobDto jobDto = modelMapper.map(job, JobDto.class);

        assertEquals(jobDto.getName(), job.getName());
        assertEquals(jobDto.getSalary(), job.getSalary());
        assertEquals(jobDto.getStartDate(), job.getStartDate());
        assertEquals(jobDto.getEndDate(), job.getEndDate());
    }
}