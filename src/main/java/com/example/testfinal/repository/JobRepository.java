package com.example.testfinal.repository;

import com.example.testfinal.model.Job;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    @NotNull Page<Job> findAll(@NotNull Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Job> findWithLockById(long id);
}
