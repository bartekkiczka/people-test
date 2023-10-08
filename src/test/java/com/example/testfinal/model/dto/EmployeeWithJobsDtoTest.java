package com.example.testfinal.model.dto;

import com.example.testfinal.exceptions.impl.job.JobNotFoundException;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Job;
import com.example.testfinal.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class EmployeeWithJobsDtoTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JobRepository jobRepository;

    @Test
    public void testEmployeeToEmployeeWithJobsDtoConversion(){
        Employee employee = Employee.builder()
                .name("name")
                .surname("surname")
                .pesel(66553315782L)
                .height(60)
                .weight(40)
                .email("employee@mail.com")
                .employmentStartDate(LocalDate.of(2021, 2, 2))
                .position("position")
                .salary(200)
                .build();

        Job job = jobRepository.findById(1L).orElseThrow(() -> new JobNotFoundException(1));
        employee.getJobs().add(job);

        EmployeeWithJobsDto dto = modelMapper.map(employee, EmployeeWithJobsDto.class);

        assertEquals(dto.getId(), employee.getId());
        assertEquals(dto.getName(), employee.getName());
        assertEquals(dto.getSurname(), employee.getSurname());
        assertEquals(dto.getPesel(), employee.getPesel());
        assertEquals(dto.getHeight(), employee.getHeight());
        assertEquals(dto.getWeight(), employee.getWeight());
        assertEquals(dto.getEmail(), employee.getEmail());
        assertEquals(dto.getEmploymentStartDate(), employee.getEmploymentStartDate());
        assertEquals(dto.getPosition(), employee.getPosition());
        assertEquals(dto.getSalary(), employee.getSalary());

        List<Long> dtoJobIds = dto.getJobs().stream().map(JobDto::getId).toList();
        List<Long> employeeJobIds = employee.getJobs().stream().map(Job::getId).toList();
        assertTrue(dtoJobIds.containsAll(employeeJobIds));

    }
}
