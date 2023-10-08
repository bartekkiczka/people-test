package com.example.testfinal.service;

import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.exceptions.impl.job.JobAlreadyAssignedException;
import com.example.testfinal.exceptions.impl.job.JobDatesOverlapException;
import com.example.testfinal.exceptions.impl.job.JobNotFoundException;
import com.example.testfinal.exceptions.impl.person.*;
import com.example.testfinal.exceptions.impl.validation.ParameterNotExistsException;
import com.example.testfinal.factory.person.PersonFactory;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Job;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.create.CreatePersonCommand;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import com.example.testfinal.queryBuilder.search.PersonSearchQueryBuilder;
import com.example.testfinal.queryBuilder.search.mappers.PersonMapperImpl;
import com.example.testfinal.repository.JobRepository;
import com.example.testfinal.repository.PersonRepository;
import com.example.testfinal.requests.SearchRequest;
import com.example.testfinal.editor.person.PersonEditorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
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
    private final PersonEditorHandler personEditorHandler;
    private final JobRepository jobRepository;
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

    public List<Person> search(SearchRequest searchRequest, Pageable pageable) {
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
            return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
                try {
                    return personMapper.map(rs);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PersonMappingException();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterNotExistsException();
        }
    }

    public List<Employee> findAllEmployeesWithJobs() {
        return personRepository.findAllEmployeesWithJobs();
    }

    public List<Job> getEmployeeJobs(long id) {
        return personRepository.findById(id)
                .map(person -> {
                    if (person.getType() != PersonTypes.EMPLOYEE) {
                        throw new PersonIsNotEmployeeException(id);
                    }
                    Employee employee = (Employee) person;
                    return employee.getJobs();
                }).orElseThrow(() -> new PersonNotFoundException(id));
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

    @Transactional
    public void addEmployeeJob(long employeeId, long jobId) {
        Person person = personRepository
                .findWithLockById(employeeId).orElseThrow(() -> new PersonNotFoundException(employeeId));
        Job job = jobRepository.findWithLockById(jobId).orElseThrow(() -> new JobNotFoundException(jobId));

        if (job.getEmployee() != null) {
            throw new JobAlreadyAssignedException();
        }

        if (person.getType() != PersonTypes.EMPLOYEE) {
            throw new PersonIsNotEmployeeException(employeeId);
        }

        Employee employee = (Employee) person;

        List<Job> employeeJobs = employee.getJobs();
        employeeJobs.forEach(employeeJob -> {
            if (job.getStartDate().isBefore(employeeJob.getEndDate()) && job.getEndDate().isAfter(employeeJob.getStartDate())) {
                throw new JobDatesOverlapException();
            }
        });

        employee.getJobs().add(job);
        job.setEmployee(employee);

        personRepository.save(employee);
        jobRepository.save(job);
    }

    @Transactional
    public void removeEmployeeJob(long employeeId, long jobId) {
        Person person = personRepository
                .findWithLockById(employeeId).orElseThrow(() -> new PersonNotFoundException(employeeId));
        Job job = jobRepository.findWithLockById(jobId).orElseThrow(() -> new JobNotFoundException(jobId));

        if (person.getType() != PersonTypes.EMPLOYEE) {
            throw new PersonIsNotEmployeeException(employeeId);
        }

        Employee employee = (Employee) person;
        List<Job> employeeJobs = employee.getJobs();

        if (!employeeJobs.contains(job)) {
            throw new EmployeeDoesNotContainJobException(employeeId, jobId);
        }

        job.setEmployee(null);
        employeeJobs.remove(job);

        jobRepository.save(job);
        employeeJobs.remove(job);
    }
}
