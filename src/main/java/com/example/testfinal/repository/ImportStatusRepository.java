package com.example.testfinal.repository;

import com.example.testfinal.model.ImportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImportStatusRepository extends JpaRepository<ImportStatus, Long> {

    Page<ImportStatus> findAll(Pageable pageable);

    @Query("SELECT i FROM ImportStatus i WHERE i.status <> 'QUEUED' ORDER BY i.id DESC LIMIT 1")
    Optional<ImportStatus> findTopOrderByIdDescWithNoQueuedStatus();
}
