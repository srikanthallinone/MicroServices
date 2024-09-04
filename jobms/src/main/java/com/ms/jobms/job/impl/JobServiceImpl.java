package com.ms.jobms.job.impl;

import com.ms.jobms.external.Company;
import com.ms.jobms.external.Review;
import com.ms.jobms.job.Job;
import com.ms.jobms.job.JobRepository;
import com.ms.jobms.job.JobService;
import com.ms.jobms.job.clients.CompanyClient;
import com.ms.jobms.job.clients.ReviewClient;
import com.ms.jobms.job.dto.JobDTO;
import com.ms.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    //private List<Job> jobs = new ArrayList<>();
    //private Long id= 1L;
    JobRepository jobRepository;
    @Autowired
    RestTemplate restTemplate;
    private CompanyClient companyClient;
    private ReviewClient reviewClient;
    int count=0;
    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient,ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
   // @CircuitBreaker(name="companyBreaker",fallbackMethod = "companyBreakerFallBack")
    //@Retry(name="companyBreaker",fallbackMethod = "companyBreakerFallBack")
    @RateLimiter(name="companyBreaker",fallbackMethod = "companyBreakerFallBack")
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        count=count+1;
        System.out.println("ATTEMP:" + count);
        //return createJobWithCompanyDTOS(jobs);
        return jobs.stream().map(this::createJobWithCompanyDTO).collect(Collectors.toList());
    }
    public List<String> companyBreakerFallBack(Exception e) {
         List<String> list =new ArrayList<>();
        list.add("Dummy");
        return list;
    }
    private JobDTO createJobWithCompanyDTO(Job job) {
        //Company company= restTemplate.getForObject("http://COMPANY-SERVICE:8081/companies/"+job.getCompanyId(), Company.class);

        Company company = companyClient.getCompany(job.getCompanyId());
      /*  ResponseEntity<List<Review>> reviewResposne  = restTemplate.exchange("http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(), HttpMethod.GET, null
                , new ParameterizedTypeReference<List<Review>>() {
                });
        List<Review> reviews = reviewResposne.getBody();*/
        List<Review> reviews =reviewClient.getReviews(job.getCompanyId());
        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job,company,reviews);
       return jobDTO;
    }

    private  List<JobDTO>  createJobWithCompanyDTOS(List<Job> jobs) {
        List<JobDTO> jobDTOS = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        if (!jobs.isEmpty()) {
            for(Job job :jobs) {
                Company company= restTemplate.getForObject("http://localhost:8081/companies/"+job.getCompanyId(), Company.class);
                JobDTO jobDTO = JobMapper.mapToJobWithCompanyDTO(job,company,null);
                jobDTOS.add(jobDTO);
            }

        }
        return jobDTOS;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return createJobWithCompanyDTO(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            if(jobRepository.findById(id).isPresent()) {
                jobRepository.deleteById(id);
                return true;
            } else {
                return false;
            }

        } catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean updateJobById(Long id,Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
            if (jobOptional.isPresent()){
                Job job = jobOptional.get();
                job.setDescription(updatedJob.getDescription());
                job.setLocation(updatedJob.getLocation());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setTitle(updatedJob.getTitle());
                jobRepository.save(job);
                return true;
            }
        return false;
    }



}
