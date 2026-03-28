package com.example.fieldservice.repository;

import com.example.fieldservice.domain.Estimate;
import com.example.fieldservice.domain.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    @Query("""
        select e from Estimate e
        join e.job j
        where (:status is null or e.progressStatus = :status)
          and (:keyword = '' or lower(j.customerName) like lower(concat('%', :keyword, '%'))
               or lower(j.siteName) like lower(concat('%', :keyword, '%'))
               or lower(coalesce(e.title, '')) like lower(concat('%', :keyword, '%')))
        order by e.createdAt desc
    """)
    List<Estimate> search(@Param("keyword") String keyword, @Param("status") ProgressStatus status);

    @Query("""
        select e from Estimate e
        join fetch e.job j
        where j.id = :jobId
        order by e.createdAt desc
    """)
    List<Estimate> findByJobIdOrderByCreatedAtDesc(@Param("jobId") Long jobId);
}
