package com.example.testfinal.service;

import com.example.testfinal.editor.person.PersonEditorHandler;
import com.example.testfinal.exceptions.impl.person.*;
import com.example.testfinal.exceptions.impl.validation.ParameterNotExistsException;
import com.example.testfinal.factory.person.PersonFactory;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.create.CreatePersonCommand;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import com.example.testfinal.queryBuilder.search.PersonSearchQueryBuilder;
import com.example.testfinal.queryBuilder.search.mappers.PersonMapperImpl;
import com.example.testfinal.repository.PersonRepository;
import com.example.testfinal.requests.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonFactory personFactory;
    private final PersonEditorHandler<Person> personEditorHandler;
    private final JdbcTemplate jdbcTemplate;
    private final PersonMapperImpl personMapper;

    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public Person addPerson(CreatePersonCommand command) {
        return personRepository.save(personFactory.create(command));
    }

    public Person findById(long id) {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    public Page<Person> search(SearchRequest searchRequest, Pageable pageable) {
        StringBuilder sql = new StringBuilder("SELECT * from Person");

        Map<String, Object> map = searchRequest.getParameters();
        if (map.values().stream().allMatch(Objects::isNull)) {
            throw new InvalidOrNoParameterProvidedException();
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                sql.append(sql.toString().contains("WHERE") ? " AND " : " WHERE ");
                sql.append(PersonSearchQueryBuilder.buildSearchQuery(entry));
            }
        }
        sql.append(" AND deleted = false");
        sql.append(" LIMIT ").append(pageable.getPageSize());
        sql.append(" OFFSET ").append(pageable.getPageNumber());

        try {
            List<Person> result = jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
                try {
                    return personMapper.map(rs);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PersonMappingException();
                }
            });

            String countSql = "SELECT COUNT(1) FROM Person WHERE deleted=false";
            Integer totalItems = jdbcTemplate.queryForObject(countSql, Integer.class);
            if (totalItems == null) {
                totalItems = 0;
            }

            return new PageImpl<>(result, pageable, totalItems);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterNotExistsException();
        }
    }

    public List<Employee> findAllEmployeesWithJobs() {
        return personRepository.findAllEmployeesWithJobs();
    }

    @Transactional
    public Person edit(long id, EditPersonCommand editPersonCommand) {
        Person personToEdit = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
        try {
            personEditorHandler.edit(personToEdit, editPersonCommand);
        } catch (Exception e) {
            throw new WrongParametersException();
        }

        try {
            return personRepository.save(personToEdit);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException("Person's data has been modified by another transaction", e);
        }
    }

    public void deleteById(long id) {
        boolean containingJobs = personRepository.checkIfEmployeeContainsJobs(id);
        if (containingJobs) {
            throw new EmployeeContainsJobsException();
        }
        personRepository.deleteById(id);
    }
}
