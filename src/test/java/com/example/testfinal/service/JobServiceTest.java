package com.example.testfinal.service;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.exceptions.impl.job.JobAlreadyAssignedException;
import com.example.testfinal.exceptions.impl.job.JobDatesOverlapException;
import com.example.testfinal.exceptions.impl.job.JobEndDateIsBeforeStartDateException;
import com.example.testfinal.exceptions.impl.job.JobNotFoundException;
import com.example.testfinal.exceptions.impl.person.EmployeeDoesNotContainJobException;
import com.example.testfinal.exceptions.impl.person.PersonIsNotEmployeeException;
import com.example.testfinal.exceptions.impl.person.PersonNotFoundException;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Job;
import com.example.testfinal.repository.PersonRepository;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JobServiceTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

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

    @Test
    @Transactional
    public void testAddEmployeeJobShouldAddJob() {
        //given
        Employee employee = (Employee) personService.findById(2);
        Job job = jobService.findById(1);
        assertEquals(jobService.getEmployeeJobs(employee.getId()).size(), 0);

        //when
        jobService.addEmployeeJob(employee.getId(), job.getId());

        Employee updatedEmployee = (Employee) personService.findById(employee.getId());
        List<Job> employeeJobs = updatedEmployee.getJobs();

        //then
        assertTrue(employeeJobs.stream().anyMatch(j -> j.getId().equals(job.getId())));
    }

    @Test
    @Transactional
    public void testAddEmployeeJobShouldThrowJobDatesOverlapException() {
        //given
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
        personRepository.save(employee);

        Job job = Job.builder()
                .name("job")
                .salary(5000L)
                .startDate(LocalDate.of(2020, 1, 1))
                .endDate(LocalDate.of(2021, 1, 1))
                .build();
        jobService.save(job);

        Job overlappingJob = Job.builder()
                .name("overlapping job")
                .salary(5000L)
                .startDate(LocalDate.of(2020, 5, 1))
                .endDate(LocalDate.of(2021, 5, 1))
                .build();
        jobService.save(overlappingJob);

        assertEquals(jobService.getEmployeeJobs(employee.getId()).size(), 0);

        //when
        jobService.addEmployeeJob(employee.getId(), job.getId());

        //then
        assertThrows(JobDatesOverlapException.class, () -> jobService.addEmployeeJob(employee.getId(), overlappingJob.getId()));
    }

    @Test
    @Transactional
    public void testConcurrentAddJobShouldThrowJobAlreadyAssignedException() {
        //given
        assertEquals(jobService.getEmployeeJobs(2).size(), 0);

        //when
        jobService.addEmployeeJob(2, 1);

        //then
        assertThrows(JobAlreadyAssignedException.class, () -> jobService.addEmployeeJob(2, 1));
    }

    @Test
    public void testAddJobShouldThrowExceptionIfPersonIsNotEmployee() {
        assertThrows(PersonIsNotEmployeeException.class, () -> jobService.addEmployeeJob(1, 1));
    }

    @Test
    @Transactional
    public void testRemoveEmployeeJobShouldRemoveJob() {
        //given
        assertEquals(jobService.getEmployeeJobs(2).size(), 0);
        jobService.addEmployeeJob(2, 1);
        Job job = jobService.findById(1);
        Employee employee = (Employee) personService.findById(2);
        assertEquals(job.getEmployee().getId(), 2);
        assertTrue(employee.getJobs().stream().anyMatch(employeeJob -> Objects.equals(employeeJob.getId(), job.getId())));

        //when
        jobService.removeEmployeeJob(2, 1);

        //then
        assertNull(job.getEmployee());
        assertFalse(employee.getJobs().stream().anyMatch(employeeJob -> Objects.equals(employeeJob.getId(), job.getId())));
    }

    @Test
    @Transactional
    public void testGetEmployeeJobsShouldReturnJobs(){
        //given
        jobService.addEmployeeJob(2,1);

        //when
        List<Job> employeeJobs = jobService.getEmployeeJobs(2);

        //then
        assertEquals(employeeJobs.size(), 1);
    }

    @Test
    public void testGetEmployeeJobsShouldThrowPersonIsNotEmployeeException(){
        assertThrows(PersonIsNotEmployeeException.class, () -> jobService.getEmployeeJobs(1));
    }

    @Test
    public void testGetEmployeeJobsShouldThrowPersonNotFoundException(){
        assertThrows(PersonNotFoundException.class, () -> jobService.getEmployeeJobs(999));
    }

    @Test
    @Transactional
    public void testRemoveEmployeeJobShouldThrowEmployeeDoesNotContainJobException() {
        //given
        Employee employee = (Employee) personService.findById(2);
        assertFalse(employee.getJobs().stream().anyMatch(employeeJob -> employeeJob.getId() == 1));

        //when
        //then
        assertThrows(EmployeeDoesNotContainJobException.class, () -> jobService.removeEmployeeJob(2, 1));
    }

    @Test
    public void testRemoveEmployeeJobShouldThrowPersonIsNotEmployeeException(){
        assertThrows(PersonIsNotEmployeeException.class, () -> jobService.removeEmployeeJob(1, 1));
    }

}