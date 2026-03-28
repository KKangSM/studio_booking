package com.example.fieldservice.service;

import com.example.fieldservice.domain.Job;
import com.example.fieldservice.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return jobRepository.findAll();
        }
        return jobRepository.findByCustomerNameContainingIgnoreCaseOrSiteNameContainingIgnoreCase(keyword, keyword);
    }

    public Job save(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Job findById(Long id) {
        return jobRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 현장 정보를 찾을 수 없습니다. id=" + id));
    }
}
