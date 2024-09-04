package com.ms.jobms.job.clients;

import com.ms.jobms.external.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="COMPANY-SERVICE", url="${company-service.url}")
public interface CompanyClient {
    @GetMapping("/companies/{id}")
   Company getCompany(@PathVariable("id") Long id);
}
