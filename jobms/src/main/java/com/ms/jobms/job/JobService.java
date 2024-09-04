package com.ms.jobms.job;

import com.ms.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {
    List<JobDTO> findAll();
    void createJob(Job job);
    JobDTO getJobById(Long id);
    boolean deleteJobById(Long id);
    Boolean updateJobById(Long id,Job updatedJob);
}
