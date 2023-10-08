package com.example.testfinal.service;

import com.example.testfinal.exceptions.impl.job.JobEndDateIsBeforeStartDateException;
import com.example.testfinal.exceptions.impl.job.JobNotFoundException;
import com.example.testfinal.model.Job;
import com.example.testfinal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

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
}
