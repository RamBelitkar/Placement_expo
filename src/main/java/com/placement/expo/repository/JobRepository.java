package com.placement.expo.repository;

import com.placement.expo.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE " +
           "LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(j.company) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Job> search(String query, Pageable pageable);
    
    List<Job> findByCompanyAndStatus(String company, Job.JobStatus status);
}
