package com.example.testfinal.mappings.dto;

import com.example.testfinal.model.Employee;
import com.example.testfinal.model.dto.EmployeeWithJobsDto;
import com.example.testfinal.model.dto.JobDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.List;

public class EmployeeToEmployeeWithJobsDtoConverter implements Converter<Employee, EmployeeWithJobsDto> {
    @Override
    public EmployeeWithJobsDto convert(MappingContext<Employee, EmployeeWithJobsDto> mappingContext) {
        Employee employee = mappingContext.getSource();

        List<JobDto> jobDtos = employee.getJobs().stream().map(job -> JobDto.builder()
                .id(job.getId())
                .name(job.getName())
                .salary(job.getSalary())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .employeeId(job.getEmployee().getId())
                .build()).toList();

        return EmployeeWithJobsDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .pesel(employee.getPesel())
                .height(employee.getHeight())
                .weight(employee.getWeight())
                .email(employee.getEmail())
                .employmentStartDate(employee.getEmploymentStartDate())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .jobs(jobDtos)
                .build();
    }
}
