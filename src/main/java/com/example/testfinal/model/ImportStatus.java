package com.example.testfinal.model;

import com.example.testfinal.enums.UploadStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@SQLDelete(sql = "update import_status set deleted = true where id = ?")
@Where(clause = "deleted=false")
@EntityListeners(AuditingEntityListener.class)
public class ImportStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private UploadStatus status;

    @Column(name = "start_date", updatable = false)
    @CreatedDate
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @LastModifiedDate
    private LocalDateTime endDate;

    @Column(name = "processed_rows")
    private Long processedRows;

    private Integer timeout;

    private boolean deleted = false;
}
