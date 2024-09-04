package com.ms.firstjobapp.company;


import java.util.List;

public interface CompanyService {

    List<Company> findAll();
    void createCompany(Company company);
    Company getCompanyById(Long id);
    boolean deleteCompanyById(Long id);
    Boolean updateCompanyById(Long id,Company updatedCompany);
}
