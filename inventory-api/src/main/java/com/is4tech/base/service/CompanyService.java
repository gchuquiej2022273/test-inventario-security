package com.is4tech.base.service;

import com.is4tech.base.domain.Company;
import com.is4tech.base.dto.CompanyDto;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company saveCompany(CompanyDto input){

        validateCompanyInput(input);

        Company company = new Company();
        company.setName(input.getName());
        company.setDescription(input.getDescription());
        company.setPhone(input.getPhone());
        company.setAddress(input.getAddress());
        company.setStatus(true);

        return companyRepository.save(company);
    }

    public Company updateCompany(CompanyDto input, Integer id) throws Exception{
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Company not found"));

        validateCompanyInput(input);

        existingCompany.setName(input.getName());
        existingCompany.setDescription(input.getDescription());
        existingCompany.setPhone(input.getPhone());
        existingCompany.setAddress(input.getAddress());
        if (input.getStatus() != null){
            existingCompany.setStatus(input.getStatus());
        }
        return companyRepository.save(existingCompany);
    }

    public void deleteCompany(Integer id){
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Company not found"));
        companyRepository.delete(existingCompany);
    }

    public List<Company> getCompanies(){
        List<Company> companies = companyRepository.findAll();
        if (companies.isEmpty()){
            throw new Exceptions("No companies found");
        }
        return companies;
    }

    public Company getCompanyId(Integer id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Company not found with id: " + id));
    }

    private void validateCompanyInput(CompanyDto input) {
        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getDescription() == null || input.getDescription().trim().isEmpty()) {
            throw new Exceptions("Description cannot be empty");
        }
        if (input.getPhone() == null || input.getPhone().trim().isEmpty()) {
            throw new Exceptions("Phone cannot be empty");
        }
        if (input.getAddress() == null || input.getAddress().trim().isEmpty()) {
            throw new Exceptions("Address cannot be empty");
        }
        if (companyRepository.existsByName(input.getName())) {
            throw new Exceptions("The name already exists");
        }
    }
}
