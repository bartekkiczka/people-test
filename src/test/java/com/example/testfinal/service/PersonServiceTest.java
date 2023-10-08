package com.example.testfinal.service;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.exceptions.impl.job.JobAlreadyAssignedException;
import com.example.testfinal.exceptions.impl.job.JobDatesOverlapException;
import com.example.testfinal.exceptions.impl.job.JobNotFoundException;
import com.example.testfinal.exceptions.impl.person.*;
import com.example.testfinal.exceptions.impl.validation.ParameterNotExistsException;
import com.example.testfinal.model.*;
import com.example.testfinal.model.command.create.CreatePersonCommand;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import com.example.testfinal.repository.JobRepository;
import com.example.testfinal.repository.PersonRepository;
import com.example.testfinal.requests.SearchRequest;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    public void setup() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    public void testFindAll() {
        // Given
        List<Person> people = personService.findAll(PageRequest.of(0, 10)).getContent();

        //then
        assertEquals(3, people.size());
    }

    @Test
    public void testAddPersonShouldAddNewPerson() {
        //given
        Pensioner pensioner = Pensioner.builder()
                .name("pensioner")
                .surname("surname")
                .pesel(12345672905L)
                .height(50)
                .weight(50)
                .email("testpensioner@gmail.com")
                .pension(2000)
                .yearsWorked(40)
                .build();

        //when
        assertThrows(PersonNotFoundException.class, () -> personService.findById(4));
        personRepository.save(pensioner);
        Pensioner savedPensioner = (Pensioner) personService.findById(4);

        //then
        assertEquals(savedPensioner.getId(), 4);
        assertEquals(savedPensioner.getType(), PersonTypes.PENSIONER);
        assertEquals(savedPensioner.getName(), "pensioner");
        assertEquals(savedPensioner.getSurname(), "surname");
        assertEquals(savedPensioner.getPesel(), 12345672905L);
        assertEquals(savedPensioner.getHeight(), 50);
        assertEquals(savedPensioner.getWeight(), 50);
        assertEquals(savedPensioner.getEmail(), "testpensioner@gmail.com");
        assertEquals(savedPensioner.getPension(), 2000);
        assertEquals(savedPensioner.getYearsWorked(), 40);
    }

    @Test
    public void testAddPersonShouldReturnPersonTypeNotExistException() {
        CreatePersonCommand command = CreatePersonCommand.builder()
                .type("X")
                .parameters(new HashMap<>())
                .build();

        assertThrows(PersonTypeNotExistException.class, () -> personService.addPerson(command));
    }

    @Test
    public void testAddPersonShouldThrowExceptionWhenViolatingEmailConstraint() {
        //given
        Pensioner pensioner = Pensioner.builder()
                .name("pensioner")
                .surname("surname")
                .pesel(12345672905L)
                .height(50)
                .weight(50)
                .email("email1@test.com")
                .pension(2000)
                .yearsWorked(40)
                .build();

        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> personRepository.save(pensioner));
    }

    @Test
    public void testAddPersonShouldThrowExceptionWhenViolatingPeselConstraint() {
        //given
        Pensioner pensioner = Pensioner.builder()
                .name("pensioner")
                .surname("surname")
                .pesel(91862494108L)
                .height(50)
                .weight(50)
                .email("email199@test.com")
                .pension(2000)
                .yearsWorked(40)
                .build();

        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> personRepository.save(pensioner));
    }

    @Test
    public void testFindByIdShouldReturnPerson() {
        //given
        Student student = Student.builder()
                .name("student")
                .surname("studentSurname")
                .pesel(33445566123L)
                .height(50)
                .weight(30)
                .email("tstudent@mail.com")
                .schoolName("school")
                .yearOfStudy(3)
                .fieldOfStudy("field")
                .scholarship(300)
                .build();

        personRepository.save(student);

        //when
        Student foundStudent = (Student) personService.findById(student.getId());

        //then
        assertEquals(foundStudent.getId(), student.getId());
        assertEquals(foundStudent.getName(), student.getName());
        assertEquals(foundStudent.getSurname(), student.getSurname());
        assertEquals(foundStudent.getPesel(), student.getPesel());
        assertEquals(foundStudent.getHeight(), student.getHeight());
        assertEquals(foundStudent.getWeight(), student.getWeight());
        assertEquals(foundStudent.getEmail(), student.getEmail());
        assertEquals(foundStudent.getSchoolName(), student.getSchoolName());
        assertEquals(foundStudent.getYearOfStudy(), student.getYearOfStudy());
        assertEquals(foundStudent.getFieldOfStudy(), student.getFieldOfStudy());
        assertEquals(foundStudent.getScholarship(), student.getScholarship());
    }

    @Test
    public void testFindByIdShouldThrowPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> personService.findById(999));
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByType() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("personType", "STUDENT");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();

        List<Person> result = personService.search(searchRequest, PageRequest.of(0, 10));

        assertEquals(1, result.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByName() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name1");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(new HashMap<>(params))
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10)
        );

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonBySurname() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("surname", "surname1");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByPesel() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("pesel", 91862494108L);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByHeight() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("minHeight", 10);
        params.put("maxHeight", 20);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(2, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByWeight() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("minWeight", 10);
        params.put("maxWeight", 20);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(2, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByEmail() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("email", "email1@test.com");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByEmploymentStartDate() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("minEmploymentStartDate", LocalDate.of(2004, 1, 1));
        params.put("maxEmploymentStartDate", LocalDate.of(2006, 1, 1));
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByPosition() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("position", "position1");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonBySalary() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("minSalary", 4500);
        params.put("maxSalary", 5500);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonBySchoolName() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("schoolName", "school");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByYearOfStudy() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("minYearOfStudy", 1);
        params.put("maxYearOfStudy", 3);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByFieldOfStudy() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("fieldOfStudy", "field");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByScholarship() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("minScholarship", 50);
        params.put("maxScholarship", 150);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByPension() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("minPension", 500);
        params.put("maxPension", 3000);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldReturnMatchingPersonByYearsWorked() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("minYearsWorked", 10);
        params.put("maxYearsWorked", 30);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        //When
        List<Person> resultPage = personService.search(searchRequest, PageRequest.of(0, 10));

        //Then
        assertEquals(1, resultPage.size());
    }

    @Test
    public void testSearchShouldThrowExceptionIfParameterNotExists() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("abc", "bca");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();

        //when
        //then
        assertThrows(ParameterNotExistsException.class, () -> personService.search(searchRequest, PageRequest.of(0, 10)));
    }

    @Test
    public void testFindEmployeesWithJobsShouldReturnEmployees(){
        List<Employee> employees = personService.findAllEmployeesWithJobs();

        assertEquals(employees.size(), 1);
    }

    @Test
    public void testEditShouldEditPerson() {
        //given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "student");
        parameters.put("surname", "surname");
        parameters.put("pesel", 12345567789L);
        parameters.put("height", 50);
        parameters.put("weight", 50);
        parameters.put("email", "student1@mail.com");
        parameters.put("schoolName", "someschool");
        parameters.put("yearOfStudy", 5);
        parameters.put("fieldOfStudy", "field2");
        parameters.put("scholarship", 200);

        EditPersonCommand editPersonCommand = EditPersonCommand.builder()
                .parameters(parameters)
                .build();

        //when
        Student dbStudent = (Student) personService.findById(1);
        long versionBeforeEdit = dbStudent.getVersion();

        assertEquals(dbStudent.getName(), "name1");
        assertEquals(dbStudent.getSurname(), "surname1");
        assertEquals(dbStudent.getPesel(), 91862494108L);
        assertEquals(dbStudent.getHeight(), 10);
        assertEquals(dbStudent.getWeight(), 10);
        assertEquals(dbStudent.getEmail(), "email1@test.com");
        assertEquals(dbStudent.getSchoolName(), "school");
        assertEquals(dbStudent.getYearOfStudy(), 2);
        assertEquals(dbStudent.getFieldOfStudy(), "field");
        assertEquals(dbStudent.getScholarship(), 100);

        personService.edit(1, editPersonCommand);

        //then
        Student studentAfterEdit = (Student) personService.findById(1);

        assertEquals(studentAfterEdit.getName(), "student");
        assertEquals(studentAfterEdit.getSurname(), "surname");
        assertEquals(studentAfterEdit.getPesel(), 12345567789L);
        assertEquals(studentAfterEdit.getHeight(), 50);
        assertEquals(studentAfterEdit.getWeight(), 50);
        assertEquals(studentAfterEdit.getEmail(), "student1@mail.com");
        assertEquals(studentAfterEdit.getSchoolName(), "someschool");
        assertEquals(studentAfterEdit.getYearOfStudy(), 5);
        assertEquals(studentAfterEdit.getFieldOfStudy(), "field2");
        assertEquals(studentAfterEdit.getScholarship(), 200);
        assertTrue(studentAfterEdit.getVersion() > versionBeforeEdit);
    }

    @Test
    public void testConcurrentEditShouldThrowOptimisticLockingException() {
        //given
        Student studentToAdd = Student.builder()
                .name("student1")
                .surname("studentSurname1")
                .pesel(33445566123L)
                .height(50)
                .weight(30)
                .email("tstudent@mail.com")
                .schoolName("school")
                .yearOfStudy(3)
                .fieldOfStudy("field")
                .scholarship(300)
                .build();
        personRepository.save(studentToAdd);

        //when
        Student student1 = (Student) personService.findById(studentToAdd.getId());
        Student student2 = (Student) personService.findById(studentToAdd.getId());

        student1.setName("update1");
        student1.setName("update2");

        personRepository.save(student1);

        //then
        assertThrows(OptimisticLockingFailureException.class, () -> personRepository.save(student2));
    }

    @Test
    public void testEditShouldThrowPersonNotFoundException() {
        //given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "student");
        parameters.put("surname", "surname");
        parameters.put("pesel", 12345567789L);
        parameters.put("height", 50);
        parameters.put("weight", 50);
        parameters.put("email", "student1@mail.com");
        parameters.put("schoolName", "someschool");
        parameters.put("yearOfStudy", 5);
        parameters.put("fieldOfStudy", "field2");
        parameters.put("scholarship", 200);

        EditPersonCommand editPersonCommand = EditPersonCommand.builder()
                .parameters(parameters)
                .build();

        //when
        //then
        assertThrows(PersonNotFoundException.class, () -> personService.edit(999, editPersonCommand));
    }

    @Test
    public void testEditShouldThrowWrongParametersException(){
        //given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "student");
        parameters.put("surname", "surname");
        parameters.put("pesel", 12345567789L);
        parameters.put("height", 50);
        parameters.put("weight", 50);
        parameters.put("email", "student1@mail.com");
        parameters.put("schoolName", "someschool");
        parameters.put("yearOfStudy", 5);
        parameters.put("fieldOfStudy", "field2");
        parameters.put("scholarship", 200);

        EditPersonCommand editPersonCommand = EditPersonCommand.builder()
                .parameters(parameters)
                .build();

        //when
        //then
        assertThrows(WrongParametersException.class, () -> personService.edit(2, editPersonCommand));
    }

    @Test
    @Transactional
    public void testAddEmployeeJobShouldAddJob() {
        //given
        Employee employee = (Employee) personService.findById(2);
        Job job = jobRepository.findById(1L).orElseThrow(() -> new JobNotFoundException(1));
        assertEquals(personService.getEmployeeJobs(employee.getId()).size(), 0);

        //when
        personService.addEmployeeJob(employee.getId(), job.getId());

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
        jobRepository.save(job);

        Job overlappingJob = Job.builder()
                .name("overlapping job")
                .salary(5000L)
                .startDate(LocalDate.of(2020, 5, 1))
                .endDate(LocalDate.of(2021, 5, 1))
                .build();
        jobRepository.save(overlappingJob);

        assertEquals(personService.getEmployeeJobs(employee.getId()).size(), 0);

        //when
        personService.addEmployeeJob(employee.getId(), job.getId());

        //then
        assertThrows(JobDatesOverlapException.class, () -> personService.addEmployeeJob(employee.getId(), overlappingJob.getId()));
    }

    @Test
    @Transactional
    public void testConcurrentAddJobShouldThrowJobAlreadyAssignedException() {
        //given
        assertEquals(personService.getEmployeeJobs(2).size(), 0);

        //when
        personService.addEmployeeJob(2, 1);

        //then
        assertThrows(JobAlreadyAssignedException.class, () -> personService.addEmployeeJob(2, 1));
    }

    @Test
    public void testAddJobShouldThrowExceptionIfPersonIsNotEmployee() {
        assertThrows(PersonIsNotEmployeeException.class, () -> personService.addEmployeeJob(1, 1));
    }

    @Test
    @Transactional
    public void testRemoveEmployeeJobShouldRemoveJob() {
        //given
        assertEquals(personService.getEmployeeJobs(2).size(), 0);
        personService.addEmployeeJob(2, 1);
        Job job = jobRepository.findById(1L).orElseThrow(() -> new JobNotFoundException(1));
        Employee employee = (Employee) personService.findById(2);
        assertEquals(job.getEmployee().getId(), 2);
        assertTrue(employee.getJobs().stream().anyMatch(employeeJob -> Objects.equals(employeeJob.getId(), job.getId())));

        //when
        personService.removeEmployeeJob(2, 1);

        //then
        assertNull(job.getEmployee());
        assertFalse(employee.getJobs().stream().anyMatch(employeeJob -> Objects.equals(employeeJob.getId(), job.getId())));
    }

    @Test
    @Transactional
    public void testGetEmployeeJobsShouldReturnJobs(){
        //given
        personService.addEmployeeJob(2,1);

        //when
        List<Job> employeeJobs = personService.getEmployeeJobs(2);

        //then
        assertEquals(employeeJobs.size(), 1);
    }

    @Test
    public void testGetEmployeeJobsShouldThrowPersonIsNotEmployeeException(){
        assertThrows(PersonIsNotEmployeeException.class, () -> personService.getEmployeeJobs(1));
    }

    @Test
    public void testGetEmployeeJobsShouldThrowPersonNotFoundException(){
        assertThrows(PersonNotFoundException.class, () -> personService.getEmployeeJobs(999));
    }

    @Test
    @Transactional
    public void testRemoveEmployeeJobShouldThrowEmployeeDoesNotContainJobException() {
        //given
        Employee employee = (Employee) personService.findById(2);
        assertFalse(employee.getJobs().stream().anyMatch(employeeJob -> employeeJob.getId() == 1));

        //when
        //then
        assertThrows(EmployeeDoesNotContainJobException.class, () -> personService.removeEmployeeJob(2, 1));
    }

    @Test
    public void testRemoveEmployeeJobShouldThrowPersonIsNotEmployeeException(){
        assertThrows(PersonIsNotEmployeeException.class, () -> personService.removeEmployeeJob(1, 1));
    }

    @Test
    public void testDeleteShouldDeletePerson() {
        //given
        assertEquals(3, personService.findAll(PageRequest.of(0, 10)).getContent().size());

        //when
        personService.deleteById(1);

        //then
        assertEquals(2, personService.findAll(PageRequest.of(0, 10)).getContent().size());
    }

    @Test
    public void testDeleteShouldNotDeleteNotExistingPerson() {
        //given
        assertEquals(3, personService.findAll(PageRequest.of(0, 10)).getContent().size());

        //when
        personService.deleteById(100);

        //then
        assertEquals(3, personService.findAll(PageRequest.of(0, 10)).getContent().size());
    }

    @Test
    @Transactional
    public void testDeleteShouldThrowEmployeeContainsJobsException(){
        assertEquals(personService.getEmployeeJobs(2).size(), 0);

        personService.addEmployeeJob(2,1);

        assertThrows(EmployeeContainsJobsException.class, () -> personService.deleteById(2));
    }
}