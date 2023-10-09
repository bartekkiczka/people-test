package com.example.testfinal.service;

import com.example.testfinal.exceptions.impl.job.JobAlreadyAssignedException;
import com.example.testfinal.exceptions.impl.job.JobDatesOverlapException;
import com.example.testfinal.exceptions.impl.job.JobEndDateIsBeforeStartDateException;
import com.example.testfinal.exceptions.impl.job.JobNotFoundException;
import com.example.testfinal.exceptions.impl.person.EmployeeDoesNotContainJobException;
import com.example.testfinal.exceptions.impl.person.PersonIsNotEmployeeException;
import com.example.testfinal.exceptions.impl.person.PersonNotFoundException;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Job;
import com.example.testfinal.model.Person;
import com.example.testfinal.repository.JobRepository;
import com.example.testfinal.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final PersonRepository personRepository;

    public Page<Job> findAll(Pageable pageable){
        return jobRepository.findAll(pageable);
    }

    public Job findById(long id){
        return jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException(id));
    }

    public Job save(Job job){
        if(job.getEndDate().isBefore(job.getStartDate())){
            throw new JobEndDateIsBeforeStartDateException();
        }

        return jobRepository.save(job);
    }

    public void deleteById(long id){
        jobRepository.deleteById(id);
    }

    public List<Job> getEmployeeJobs(long id) {
        return personRepository.findById(id)
                .map(person -> {
                    if (!person.getType().equals("EMPLOYEE")) {
                        throw new PersonIsNotEmployeeException(id);
                    }
                    Employee employee = (Employee) person;
                    return employee.getJobs();
                }).orElseThrow(() -> new PersonNotFoundException(id));
    }

    @Transactional
    public void addEmployeeJob(long employeeId, long jobId) {
        Person person = personRepository
                .findWithLockById(employeeId).orElseThrow(() -> new PersonNotFoundException(employeeId));
        Job job = jobRepository.findWithLockById(jobId).orElseThrow(() -> new JobNotFoundException(jobId));

        if (job.getEmployee() != null) {
            throw new JobAlreadyAssignedException();
        }

        if (!person.getType().equals("EMPLOYEE")) {
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

        if (!person.getType().equals("EMPLOYEE")) {
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
