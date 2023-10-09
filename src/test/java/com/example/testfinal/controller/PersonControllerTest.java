package com.example.testfinal.controller;

import com.example.testfinal.DatabaseCleaner;
import com.example.testfinal.exceptions.impl.person.PersonNotFoundException;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Job;
import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.command.create.CreatePersonCommand;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import com.example.testfinal.repository.JobRepository;
import com.example.testfinal.repository.PersonRepository;
import com.example.testfinal.requests.SearchRequest;
import com.example.testfinal.service.JobService;
import com.example.testfinal.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonControllerTest {

    private final String ADMIN_ROLE_VALUE = "Basic YWRtaW46YWRtaW4=";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @BeforeEach
    public void setup() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }

    @Test
    public void testGetPersonShouldReturnPeople() throws Exception {
        //given
        //when
        //then
        mvc.perform(get("/people"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].type").value("STUDENT"))
                .andExpect(jsonPath("$.[0].name").value("name1"))
                .andExpect(jsonPath("$.[0].surname").value("surname1"))
                .andExpect(jsonPath("$.[0].pesel").value(91862494108L))
                .andExpect(jsonPath("$.[0].height").value(10))
                .andExpect(jsonPath("$.[0].weight").value(10))
                .andExpect(jsonPath("$.[0].email").value("email1@test.com"))
                .andExpect(jsonPath("$.[0].schoolName").value("school"))
                .andExpect(jsonPath("$.[0].yearOfStudy").value(2))
                .andExpect(jsonPath("$.[0].fieldOfStudy").value("field"))
                .andExpect(jsonPath("$.[0].scholarship").value(100))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].type").value("EMPLOYEE"))
                .andExpect(jsonPath("$.[1].name").value("name2"))
                .andExpect(jsonPath("$.[1].surname").value("surname2"))
                .andExpect(jsonPath("$.[1].pesel").value(91863494108L))
                .andExpect(jsonPath("$.[1].height").value(20))
                .andExpect(jsonPath("$.[1].weight").value(20))
                .andExpect(jsonPath("$.[1].email").value("email2@test.com"))
                .andExpect(jsonPath("$.[1].employmentStartDate").value("2005-01-01"))
                .andExpect(jsonPath("$.[1].position").value("position1"))
                .andExpect(jsonPath("$.[1].salary").value(5000))
                .andExpect(jsonPath("$.[2].id").value(3))
                .andExpect(jsonPath("$.[2].type").value("PENSIONER"))
                .andExpect(jsonPath("$.[2].name").value("name3"))
                .andExpect(jsonPath("$.[2].surname").value("surname3"))
                .andExpect(jsonPath("$.[2].pesel").value(92863494108L))
                .andExpect(jsonPath("$.[2].height").value(30))
                .andExpect(jsonPath("$.[2].weight").value(30))
                .andExpect(jsonPath("$.[2].email").value("email3@test.com"))
                .andExpect(jsonPath("$.[2].pension").value(2000))
                .andExpect(jsonPath("$.[2].yearsWorked").value(30))
                .andReturn();
    }

    @Test
    public void testSearchShouldFindMatchingPerson() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name1");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        String searchRequestJson = objectMapper.writeValueAsString(searchRequest);
        //when
        //then
        mvc.perform(get("/people/search")
                        .param("name", "name1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchRequestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].type").value("STUDENT"))
                .andExpect(jsonPath("$.[0].name").value("name1"))
                .andExpect(jsonPath("$.[0].surname").value("surname1"))
                .andExpect(jsonPath("$.[0].pesel").value(91862494108L))
                .andExpect(jsonPath("$.[0].height").value(10))
                .andExpect(jsonPath("$.[0].weight").value(10))
                .andExpect(jsonPath("$.[0].email").value("email1@test.com"))
                .andExpect(jsonPath("$.[0].schoolName").value("school"))
                .andExpect(jsonPath("$.[0].yearOfStudy").value(2))
                .andExpect(jsonPath("$.[0].fieldOfStudy").value("field"))
                .andExpect(jsonPath("$.[0].scholarship").value(100))
                .andReturn();
    }

    @Test
    public void testSearchShouldNotFindPerson() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "abc");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();
        String searchRequestJson = objectMapper.writeValueAsString(searchRequest);
        //when
        //then
        mvc.perform(get("/people/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchRequestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", empty()))
                .andReturn();
    }

    @Test
    public void testSearchShouldThrowExceptionWhenParametersAreInvalid() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();

        String searchRequestJson = objectMapper.writeValueAsString(searchRequest);

        //when
        //then
        mvc.perform(get("/people/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Invalid or no parameter provided"))
                .andExpect(jsonPath("$.description").value("uri=/people/search"));
    }

    @Test
    public void testSearchShouldThrowExceptionWhenParameterNotExists() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("abc", "bca");
        SearchRequest searchRequest = SearchRequest.builder()
                .parameters(params)
                .build();

        String searchRequestJson = objectMapper.writeValueAsString(searchRequest);

        mvc.perform(get("/people/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(searchRequestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Provided parameter does not exists"))
                .andExpect(jsonPath("$.description").value("uri=/people/search"));
    }

    @Test
    public void testSaveShouldSavePerson() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("surname", "surname");
        params.put("pesel", 92813294148L);
        params.put("height", 50);
        params.put("weight", 50);
        params.put("email", "testmail@test.com");
        params.put("pension", 1000);
        params.put("yearsWorked", 30);
        CreatePersonCommand command = CreatePersonCommand.builder()
                .type("PENSIONER")
                .parameters(params)
                .build();
        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(get("/people/4"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Can't find person with id: 4"))
                .andExpect(jsonPath("$.description").value("uri=/people/4"));

        //when
        //then
        mvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson)
                        .header("Authorization", ADMIN_ROLE_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.type").value("PENSIONER"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.pesel").value(92813294148L))
                .andExpect(jsonPath("$.height").value(50))
                .andExpect(jsonPath("$.weight").value(50))
                .andExpect(jsonPath("$.email").value("testmail@test.com"))
                .andExpect(jsonPath("$.pension").value(1000))
                .andExpect(jsonPath("$.yearsWorked").value(30))
                .andReturn();

        mvc.perform(get("/people/4"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.type").value("PENSIONER"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.pesel").value(92813294148L))
                .andExpect(jsonPath("$.height").value(50))
                .andExpect(jsonPath("$.weight").value(50))
                .andExpect(jsonPath("$.email").value("testmail@test.com"))
                .andExpect(jsonPath("$.pension").value(1000))
                .andExpect(jsonPath("$.yearsWorked").value(30))
                .andReturn();
    }

    @Test
    public void testSaveShouldNotSavePersonWithoutAuthorization() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("surname", "surname");
        params.put("pesel", 92813294148L);
        params.put("height", 50);
        params.put("weight", 50);
        params.put("email", "testmail@test.com");
        params.put("pension", 1000);
        params.put("yearsWorked", 30);
        CreatePersonCommand command = CreatePersonCommand.builder()
                .type("PENSIONER")
                .parameters(params)
                .build();
        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void testSaveShouldThrowPersonTypeNotExistException() throws Exception {
        //given
        CreatePersonCommand command = CreatePersonCommand.builder()
                .type("X")
                .parameters(new HashMap<>())
                .build();
        String commandJson = objectMapper.writeValueAsString(command);

        //when
        //then
        mvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson)
                        .header("Authorization", ADMIN_ROLE_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Person of type X don't exist"))
                .andExpect(jsonPath("$.description").value("uri=/people"));
    }

    @Test
    public void testSaveShouldThrowExceptionWhenParametersAreInvalid() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", null);
        params.put("surname", "surname");
        params.put("pesel", 92813294148L);
        params.put("height", 50);
        params.put("weight", 50);
        params.put("email", "testmail@test.com");
        params.put("pension", 1000);
        params.put("yearsWorked", 30);

        CreatePersonCommand command = CreatePersonCommand.builder()
                .type("PENSIONER")
                .parameters(params)
                .build();
        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson)
                        .header("Authorization", ADMIN_ROLE_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message", containsString("NAME_IS_EMPTY")))
                .andExpect(jsonPath("$.message", containsString("NAME_IS_NULL")))
                .andExpect(jsonPath("$.description").value("uri=/people"));
    }

    @Test
    public void testEditShouldEditPerson() throws Exception {
        //given
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "updated name");
        parameters.put("surname", "surname3");
        parameters.put("pesel", 92863494108L);
        parameters.put("height", 30);
        parameters.put("weight", 30);
        parameters.put("email", "testpensioner@gmail.com");
        parameters.put("pension", 2000);
        parameters.put("yearsWorked", 30);

        EditPersonCommand editPersonCommand = EditPersonCommand.builder()
                .parameters(parameters)
                .build();


        String personJson = objectMapper.writeValueAsString(editPersonCommand);

        //when
        mvc.perform(get("/people/3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.type").value("PENSIONER"))
                .andExpect(jsonPath("$.name").value("name3"))
                .andExpect(jsonPath("$.surname").value("surname3"))
                .andExpect(jsonPath("$.pesel").value(92863494108L))
                .andExpect(jsonPath("$.height").value(30))
                .andExpect(jsonPath("$.weight").value(30))
                .andExpect(jsonPath("$.email").value("email3@test.com"))
                .andExpect(jsonPath("$.pension").value(2000))
                .andExpect(jsonPath("$.yearsWorked").value(30))
                .andReturn();

        mvc.perform(put("/people/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson)
                        .header("Authorization", ADMIN_ROLE_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.type").value("PENSIONER"))
                .andExpect(jsonPath("$.name").value("updated name"))
                .andExpect(jsonPath("$.surname").value("surname3"))
                .andExpect(jsonPath("$.pesel").value(92863494108L))
                .andExpect(jsonPath("$.height").value(30))
                .andExpect(jsonPath("$.weight").value(30))
                .andExpect(jsonPath("$.email").value("testpensioner@gmail.com"))
                .andExpect(jsonPath("$.pension").value(2000))
                .andExpect(jsonPath("$.yearsWorked").value(30))
                .andReturn();

        //then
        mvc.perform(get("/people/3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.type").value("PENSIONER"))
                .andExpect(jsonPath("$.name").value("updated name"))
                .andExpect(jsonPath("$.surname").value("surname3"))
                .andExpect(jsonPath("$.pesel").value(92863494108L))
                .andExpect(jsonPath("$.height").value(30))
                .andExpect(jsonPath("$.weight").value(30))
                .andExpect(jsonPath("$.email").value("testpensioner@gmail.com"))
                .andExpect(jsonPath("$.pension").value(2000))
                .andExpect(jsonPath("$.yearsWorked").value(30))
                .andReturn();
    }

    @Test
    public void testEditShouldNotEditPersonWithoutAuthorization() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "updated name");
        parameters.put("surname", "surname3");
        parameters.put("pesel", 92863494108L);
        parameters.put("height", 30);
        parameters.put("weight", 30);
        parameters.put("email", "testpensioner@gmail.com");
        parameters.put("pension", 2000);
        parameters.put("yearsWorked", 30);

        EditPersonCommand editPersonCommand = EditPersonCommand.builder()
                .parameters(parameters)
                .build();

        String personJson = objectMapper.writeValueAsString(editPersonCommand);

        mvc.perform(put("/people/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testEditShouldThrowPersonNotFoundException() throws Exception {
        //given
        Pensioner person = (Pensioner) personRepository.findById(3L).orElseThrow(() -> new PersonNotFoundException(3));

        String personJson = objectMapper.writeValueAsString(person);

        //when
        //then
        mvc.perform(put("/people/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", ADMIN_ROLE_VALUE)
                        .content(personJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Can't find person with id: 999"))
                .andExpect(jsonPath("$.description").value("uri=/people/999"));
    }

    @Test
    public void testEditShouldThrowWrongParametersException() throws Exception {
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
        String editPersonCommandJson = objectMapper.writeValueAsString(editPersonCommand);

        mvc.perform(put("/people/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", ADMIN_ROLE_VALUE)
                        .content(editPersonCommandJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Wrong parameters provided"))
                .andExpect(jsonPath("$.description").value("uri=/people/2"));

    }

    @Test
    public void testDeletePersonShouldReturnNoContentStatus() throws Exception {
        mvc.perform(delete("/people/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteEmployeeWithJobsShouldThrowEmployeeContainsJobException() throws Exception {
        jobService.addEmployeeJob(2, 1);

        mvc.perform(delete("/people/2"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Can't delete employee because he still contains jobs. Remove them first"))
                .andExpect(jsonPath("$.description").value("uri=/people/2"));
    }

    @Test
    public void testGetEmployeesWithJobsShouldFindEmployees() throws Exception {
        //given
        Employee employee = (Employee) personService.findById(2);
        Job job = jobService.findById(1);

        //when
        jobService.addEmployeeJob(employee.getId(), job.getId());

        //then
        mvc.perform(get("/people/employees"))
                .andDo(print())
                .andExpect(jsonPath("$.[0].id").value(2))
                .andExpect(jsonPath("$.[0].name").value("name2"))
                .andExpect(jsonPath("$.[0].surname").value("surname2"))
                .andExpect(jsonPath("$.[0].pesel").value(91863494108L))
                .andExpect(jsonPath("$.[0].height").value(20))
                .andExpect(jsonPath("$.[0].weight").value(20))
                .andExpect(jsonPath("$.[0].email").value("email2@test.com"))
                .andExpect(jsonPath("$.[0].employmentStartDate").value("2005-01-01"))
                .andExpect(jsonPath("$.[0].position").value("position1"))
                .andExpect(jsonPath("$.[0].salary").value(5000))
                .andExpect(jsonPath("$.[0].jobs[0].id").value(1))
                .andExpect(jsonPath("$.[0].jobs[0].name").value("name1"))
                .andExpect(jsonPath("$.[0].jobs[0].salary").value(2500))
                .andExpect(jsonPath("$.[0].jobs[0].startDate").value("2020-01-01"))
                .andExpect(jsonPath("$.[0].jobs[0].endDate").value("2021-01-01"))
                .andExpect(jsonPath("$.[0].jobs[0].employeeId").value(2));
    }
}