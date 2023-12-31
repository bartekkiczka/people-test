package com.example.testfinal.controller;

import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.create.CreatePersonCommand;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import com.example.testfinal.model.dto.EmployeeWithJobsDto;
import com.example.testfinal.model.dto.PersonDto;
import com.example.testfinal.requests.SearchRequest;
import com.example.testfinal.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PersonDto>> getPeople(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return new ResponseEntity<>(personService.findAll(pageable).stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeWithJobsDto>> getEmployeesWithJobs() {
        return new ResponseEntity<>(personService.findAllEmployeesWithJobs().stream()
                .map(employee -> modelMapper.map(employee, EmployeeWithJobsDto.class)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> findById(@PathVariable long id) {
        Person person = personService.findById(id);
        return new ResponseEntity<>(modelMapper.map(person, PersonDto.class), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PersonDto>> search(
            @RequestBody SearchRequest searchRequest,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        List<PersonDto> people = personService.search(searchRequest, pageable)
                .stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonDto> save(@RequestBody CreatePersonCommand command) {
        Person person = personService.addPerson(command);
        return new ResponseEntity<>(modelMapper.map(person, PersonDto.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> edit(@PathVariable long id, @RequestBody EditPersonCommand editPersonCommand) {
        Person person = personService.edit(id, editPersonCommand);
        return new ResponseEntity<>(modelMapper.map(person, PersonDto.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        personService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
