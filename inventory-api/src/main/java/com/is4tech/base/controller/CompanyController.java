package com.is4tech.base.controller;

import com.is4tech.base.domain.Company;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.CompanyDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.CompanyService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private AuditService auditService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCompany(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody CompanyDto companyDto) {
        try {
            Company createCompany = companyService.saveCompany(companyDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Company created successfully");
            auditService.createAudit(servletRequest, response, "Company", companyDto.getName(), createCompany.getId(), "Company created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Company added successfully", createCompany));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error adding company", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding company", e.getMessage()));
        }
    }

    @PutMapping("/update/{companyId}")
    public ResponseEntity<ApiResponse> updateCompany(HttpServletRequest servletRequest,HttpServletResponse response, @PathVariable("companyId") Integer id, @RequestBody CompanyDto companyDto){
        try {
            Company updateCompany = companyService.updateCompany(companyDto, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK,"Company updated successfully");
            auditService.createAudit(servletRequest, response, "Company", companyDto.getName(), updateCompany.getId(), "Company updated successfully");
            return ResponseEntity.ok(new ApiResponse("Company updated successfully", updateCompany));
        }catch (Exception e){
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating company", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating company", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{companyId}")
    public ResponseEntity<ApiResponse> deleteCompany(HttpServletRequest servletRequest,HttpServletResponse response, @PathVariable("companyId") Integer id){
        try {
            companyService.deleteCompany(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK,"Company deleted successfully");
            auditService.createAudit(servletRequest, response, "Company", null, id, "Company deleted successfully");
            return ResponseEntity.ok(new ApiResponse("Company deleted successfully", null));
        }catch (Exception e){
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting company", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting company", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getCompanies(HttpServletRequest servletRequest) {
        try {
            List<Company> companies = companyService.getCompanies();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Fetched companies list");
            return ResponseEntity.ok(new ApiResponse("Fetched companies list:", companies));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting companies", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting companies", e.getMessage()));
        }
    }

    @GetMapping("/listId/{roleId}")
    public ResponseEntity<ApiResponse> getRoleId(HttpServletRequest servletRequest, @PathVariable("roleId") Integer id){
        try{
            Company company = companyService.getCompanyId(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Company found with ID:" + id);
            return ResponseEntity.ok(new ApiResponse("Company successfully obtained", company));
        }catch (Exception e){
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting company", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error geting company ", e.getMessage()));
        }
    }
}
