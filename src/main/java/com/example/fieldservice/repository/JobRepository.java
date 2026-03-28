package com.example.fieldservice.repository;

import com.example.fieldservice.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCustomerNameContainingIgnoreCaseOrSiteNameContainingIgnoreCase(String customerKeyword, String siteKeyword);
}
