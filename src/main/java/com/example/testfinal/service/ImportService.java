package com.example.testfinal.service;

import com.example.testfinal.enums.UploadStatus;
import com.example.testfinal.factory.person.PersonFactory;
import com.example.testfinal.model.ImportStatus;
import com.example.testfinal.model.command.create.CreatePersonCommand;
import com.example.testfinal.repository.ImportStatusRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Getter
@Setter
@EnableAsync
@Slf4j
public class ImportService {

    private final String query = "insert into person (person_type, name, surname, pesel, height, weight, email, " +
            "employment_start_date, position, salary, school_name, year_of_study, field_of_study, scholarship, " +
            "pension, years_worked, deleted) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,false)";
    private final ImportStatusRepository importStatusRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PersonFactory personFactory;

    public Page<ImportStatus> findAll(Pageable pageable) {
        return importStatusRepository.findAll(pageable);
    }

    @Async("importPeopleExecutor")
    public void uploadFile(File file, Integer timeout) {
        long currentProcessedRows = 0;
        ImportStatus currentImportStatus = ImportStatus.builder()
                .timeout(timeout)
                .processedRows(0L)
                .status(UploadStatus.STARTED)
                .build();
        importStatusRepository.save(currentImportStatus);


        int batchSize = 100;
        List<Object[]> batchData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                currentProcessedRows++;
                currentImportStatus.setProcessedRows(currentProcessedRows);
                String[] args = line.split(",");
                String type = args[0].equals("null") ? null : args[0];
                String name = args[1].equals("null") ? null : args[1];
                String surname = args[2].equals("null") ? null : args[2];
                Long pesel = args[3].equals("null") ? null : Long.parseLong(args[3]);
                Integer height = args[4].equals("null") ? null : Integer.parseInt(args[4]);
                Integer weight = args[5].equals("null") ? null : Integer.parseInt(args[5]);
                String email = args[6].equals("null") ? null : args[6];
                LocalDate employmentStartDate = args[7].equals("null") ? null : LocalDate.parse(args[7]);
                String position = args[8].equals("null") ? null : args[8];
                Integer salary = args[9].equals("null") ? null : Integer.parseInt(args[9]);
                String schoolName = args[10].equals("null") ? null : args[10];
                Integer yearOfStudy = args[11].equals("null") ? null : Integer.parseInt(args[11]);
                String fieldOfStudy = args[12].equals("null") ? null : args[12];
                Integer scholarship = args[13].equals("null") ? null : Integer.parseInt(args[13]);
                Integer pension = args[14].equals("null") ? null : Integer.parseInt(args[14]);
                Integer yearsWorked = args[15].equals("null") ? null : Integer.parseInt(args[15]);

                Object[] rowData = {type, name, surname, pesel, height, weight, email, employmentStartDate, position,
                        salary, schoolName, yearOfStudy, fieldOfStudy, scholarship, pension, yearsWorked};

                Map<String, Object> params = new HashMap<>();
                params.put("name", name);
                params.put("surname", surname);
                params.put("pesel", pesel);
                params.put("height", height);
                params.put("weight", weight);
                params.put("email", email);
                params.put("employmentStartDate", employmentStartDate);
                params.put("position", position);
                params.put("salary", salary);
                params.put("schoolName", schoolName);
                params.put("yearOfStudy", yearOfStudy);
                params.put("fieldOfStudy", fieldOfStudy);
                params.put("scholarship", scholarship);
                params.put("pension", pension);
                params.put("yearsWorked", yearsWorked);

                CreatePersonCommand createPersonCommand = CreatePersonCommand.builder()
                        .type(type)
                        .parameters(params)
                        .build();

                try {
                    personFactory.create(createPersonCommand);
                    batchData.add(rowData);
                } catch (Exception e) {
                    log.error("Error while processing line number: " + currentProcessedRows, e);
                }
                if (currentProcessedRows % batchSize == 0) {
                    try {
                        jdbcTemplate.batchUpdate(query, batchData);
                        importStatusRepository.save(currentImportStatus);
                        batchData.clear();
                    } catch (RuntimeException e) {
                        updateImportStatusEnd(currentProcessedRows, currentImportStatus, UploadStatus.FAILED);
                        log.error("Error while processing line number : " + currentProcessedRows, e);
                        throw new RuntimeException("Import failed");
                    }
                }

            }
            if (!batchData.isEmpty()) {
                try {
                    jdbcTemplate.batchUpdate(query, batchData);
                } catch (RuntimeException e) {
                    updateImportStatusEnd(currentProcessedRows, currentImportStatus, UploadStatus.FAILED);
                    log.error("Error while processing line number : " + currentProcessedRows, e);
                    throw new RuntimeException("Import failed");
                }
            }
        } catch (IOException e) {
            log.error("Unable to upload file", e);
        } finally {
            file.delete();
        }
        updateImportStatusEnd(currentProcessedRows, currentImportStatus, UploadStatus.COMPLETED);
    }

    private void updateImportStatusEnd(long currentProcessedRows, ImportStatus importStatus, UploadStatus Status) {
        importStatus.setStatus(Status);
        importStatus.setProcessedRows(currentProcessedRows);
        importStatusRepository.save(importStatus);
    }
}
