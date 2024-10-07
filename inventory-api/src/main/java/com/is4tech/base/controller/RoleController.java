package com.is4tech.base.controller;

import com.is4tech.base.domain.Roles;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.RolesDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.RolesService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RolesService rolesService;
    private final AuditService auditService;
    private static final String ENTIDAD = "Role";

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRole(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody RolesDto rolesDto) {
        try {
            Roles createRole = rolesService.saveRoles(rolesDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Role created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Role added successfully", createRole));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(rolesDto), "Error creating role");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error adding role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding a role", e.getMessage()));
        }
    }

    @PutMapping("/update/{roleId}")
    public ResponseEntity<ApiResponse> updateRole(HttpServletRequest servletRequest, HttpServletResponse response, @PathVariable("roleId") Integer id, @RequestBody RolesDto rolesDto) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(rolesDto), "Role updated successfully by id");
            Roles updateRole = rolesService.updateRole(rolesDto, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Role updated successfully");
            return ResponseEntity.ok(new ApiResponse("Role updated successfully", updateRole));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(rolesDto), "Error updating role by id");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating role", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<ApiResponse> deleteRole(HttpServletRequest servletRequest, HttpServletResponse response, @PathVariable("roleId") Integer id) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(id), "Role deleted successfully by id");
            rolesService.deleteRole(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Role deleted successfully");
            return ResponseEntity.ok(new ApiResponse("Role deleted successfully", null));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(id), "Error deleting role by id");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting role", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getRoles(HttpServletRequest servletRequest, HttpServletResponse response) {
        try {
            List<Roles> roles = rolesService.getRoles();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Fetched roles list");
            return ResponseEntity.ok(new ApiResponse("Fetched roles list:", roles));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, "ErrorGetRoles", "Error getting list of roles");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting roles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting roles", e.getMessage()));
        }
    }

    @GetMapping("/listId/{roleId}")
    public ResponseEntity<ApiResponse> getRoleId(HttpServletRequest servletRequest, HttpServletResponse response, @PathVariable("roleId") Integer id) {
        try {
            Roles role = rolesService.getRoleId(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Role found with ID:" + id);
            return ResponseEntity.ok(new ApiResponse("Role successfully obtained", role));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, "ErrorGetRoleById", "Error getting role by id");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting role", e.getMessage()));
        }
    }
}
