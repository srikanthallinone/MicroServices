package com.ms.companyms.company.impl;

import com.ms.companyms.company.Company;
import com.ms.companyms.company.CompanyRepository;
import com.ms.companyms.company.CompanyService;
import com.ms.companyms.company.clients.ReviewClient;
import com.ms.companyms.company.dto.ReviewMessage;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;
    private ReviewClient reviewClient;

    public CompanyServiceImpl(CompanyRepository companyRepository,ReviewClient reviewClient
                              ) {
        this.companyRepository = companyRepository;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteCompanyById(Long id) {
        try {
            Optional<Company> companyOptional = companyRepository.findById(id);
            if(companyOptional.isPresent()) {
                companyRepository.deleteById(id);
                return true;
            } else {
                return false;
            }

        } catch (Exception e){
            return false;
        }    }

    @Override
    public Boolean updateCompanyById(Long id, Company updatedCompany) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()){
            Company company = companyOptional.get();
            company.setDescription(updatedCompany.getDescription());
            company.setName(updatedCompany.getName());
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        Company company = companyRepository.findById(reviewMessage.getCompanyId())
                .orElseThrow(()-> new NotFoundException("Company NOt found :"+reviewMessage.getCompanyId()));
        Double rating =reviewClient.getAverageRating(reviewMessage.getCompanyId());
        company.setRating(rating);
        companyRepository.save(company);
    }
}
