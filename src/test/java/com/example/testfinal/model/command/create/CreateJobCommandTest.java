package com.example.testfinal.model.command.create;

import com.example.testfinal.model.Job;
import com.example.testfinal.model.command.create.CreateJobCommand;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CreateJobCommandTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testCreateJobCommandShouldCreateJob(){
        CreateJobCommand createJobCommand = CreateJobCommand.builder()
                .name("job")
                .salary(100L)
                .startDate(LocalDate.of(2020,1,1))
                .endDate(LocalDate.of(2021,1,1))
                .build();

        Job job = modelMapper.map(createJobCommand, Job.class);

        assertEquals(job.getName(), "job");
        assertEquals(job.getSalary(), 100L);
        assertEquals(job.getStartDate(), LocalDate.of(2020,1,1));
        assertEquals(job.getEndDate(), LocalDate.of(2021,1,1));
    }
}