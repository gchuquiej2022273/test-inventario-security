package com.is4tech.base.controller;

import com.is4tech.base.dto.ProfileHasRoleCreateDto;
import com.is4tech.base.dto.ProfileHasRoleDTO;
import com.is4tech.base.service.ProfileHasRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/profile-Role")
public class ProfileHasRoleController {


    @Autowired
    private ProfileHasRoleService profileHasRoleService;


    @Autowired
    private ProfileHasRoleService profileService;


    @PostMapping
    public ResponseEntity<ProfileHasRoleDTO> createProfileHasRole(@RequestBody ProfileHasRoleCreateDto dto) {
        ProfileHasRoleDTO createdProfileHasRole = profileHasRoleService.createProfileHasRole(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfileHasRole);
    }
}
