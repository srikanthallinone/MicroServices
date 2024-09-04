package com.ms.companyms.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @GetMapping
    public ResponseEntity<List<Company>> findAll() {
        return new ResponseEntity<>( companyService.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> findById(@PathVariable Long id) {
        Company company =  companyService.getCompanyById(id);
        if(company != null) {
            return new ResponseEntity<>(company,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String>  findById(@PathVariable Long id, @RequestBody Company company) {
        boolean updated = companyService.updateCompanyById(id,company);
        if (updated) {
            return new ResponseEntity<>("Company updated successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String>  addCompany(@RequestBody Company company) {
       companyService.createCompany(company);
       return new ResponseEntity<>("Company created  successfully",HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable Long id) {
        boolean deleted =  companyService.deleteCompanyById(id);
        if(deleted) {
            return new ResponseEntity<>("company delerted sucessfully",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

/*


GET   /companies/
POST  /companies/
GET  /companies/{id}
PUT    /companies/{id}
DELETE /companies/{id}

*/
