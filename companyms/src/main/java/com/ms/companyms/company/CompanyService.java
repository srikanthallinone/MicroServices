package com.ms.companyms.company;


import com.ms.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {

    List<Company> findAll();
    void createCompany(Company company);
    Company getCompanyById(Long id);
    boolean deleteCompanyById(Long id);
    Boolean updateCompanyById(Long id,Company updatedCompany);
    public void updateCompanyRating(ReviewMessage reviewMessage);
}
