package com.example.testfinal.repository;

import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Person;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @NotNull Page<Person> findAll(@NotNull Pageable pageable);

    @Query("SELECT p FROM Person p LEFT JOIN FETCH p.jobs j WHERE p.type = EMPLOYEE")
    List<Employee> findAllEmployeesWithJobs();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Person> findWithLockById(long id);

    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN TRUE ELSE FALSE END FROM Person p JOIN p.jobs j WHERE p.id = :id")
    boolean checkIfEmployeeContainsJobs(@Param("id") long id);
}
