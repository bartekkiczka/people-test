package com.example.testfinal.controller;

import com.example.testfinal.model.Job;
import com.example.testfinal.model.command.create.CreateJobCommand;
import com.example.testfinal.model.dto.JobDto;
import com.example.testfinal.service.JobService;
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
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<JobDto>> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        return new ResponseEntity<>(jobService.findAll(pageable).stream()
                .map(job -> modelMapper.map(job, JobDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> findById(@PathVariable long id) {
        return new ResponseEntity<>(modelMapper.map(jobService.findById(id), JobDto.class), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JobDto> save(@RequestBody CreateJobCommand jobCommand) {
        Job newJob = jobService.save(modelMapper.map(jobCommand, Job.class));
        return new ResponseEntity<>(modelMapper.map(newJob, JobDto.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        jobService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
