package com.is4tech.base.service;

import com.is4tech.base.domain.Roles;
import com.is4tech.base.dto.RolesDto;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    @Autowired
    private  RolesRepository rolesRepository;

    public Roles saveRoles(RolesDto input) {
        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getDescription() == null || input.getDescription().trim().isEmpty()) {
            throw new Exceptions("Description cannot be empty");
        }
        if (rolesRepository.existsByName(input.getName())) {
            throw new Exceptions("The name already exists");
        }

        Roles role = new Roles();
        role.setName(input.getName());
        role.setDescription(input.getDescription());
        role.setStatus(true);

        return rolesRepository.save(role);
    }

    public Roles updateRole(RolesDto input, Integer id) {
        Roles existingRole = rolesRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Role not found"));

        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getDescription() == null || input.getDescription().trim().isEmpty()) {
            throw new Exceptions("Description cannot be empty");
        }
        if (rolesRepository.existsByName(input.getName())) {
            throw new Exceptions("The name already exists");
        }

        existingRole.setName(input.getName());
        existingRole.setDescription(input.getDescription());

        if (input.getStatus() != null) {
            existingRole.setStatus(input.getStatus());
        }

        return rolesRepository.save(existingRole);
    }

    public void deleteRole(Integer id) {
        Roles existingRole = rolesRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Role not found"));

        rolesRepository.delete(existingRole);
    }

    public List<Roles> getRoles() {
        List<Roles> roles = rolesRepository.findAll();
        if (roles.isEmpty()) {
            throw new Exceptions("No roles found");
        }
        return roles;
    }

    public Roles getRoleId(Integer id) {
        return rolesRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Role not found with id: " + id));
    }
}
