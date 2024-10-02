package com.is4tech.base.controller;

import com.is4tech.base.domain.Profile;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.ProductDto;
import com.is4tech.base.dto.ProfileDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.ProfileService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AuditService auditService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProfile(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody ProfileDto profileDto) {
        try {
            Profile createdProfile = profileService.saveProfile(profileDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Profile created successfully");
            auditService.createAudit(servletRequest, response, "Profile", profileDto.getName(), createdProfile.getProfileId(), "Profile created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Profile added successfully", createdProfile));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error adding profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding a profile", e.getMessage()));
        }
    }

    @PutMapping("/update/{profileId}")
    public ResponseEntity<ApiResponse> updateProfile(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("profileId") Integer id, @RequestBody ProfileDto profile) {
        try {
            Profile updatedProfile = profileService.updateProfile(profile, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Profile updated successfully");
            auditService.createAudit(servletRequest, response, "Profile", profile.getName(),null, "Profile updated successfully");
            return ResponseEntity.ok(new ApiResponse("Profile updated successfully", updatedProfile));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating profile", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{profileId}")
    public ResponseEntity<ApiResponse> deleteProfile(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("profileId") Integer id) {
        try {
            profileService.deleteProfile(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Profile deleted successfully");
            auditService.createAudit(servletRequest, response, "Profile", null, id, "Profile deleted successfully");
            return ResponseEntity.ok(new ApiResponse("Profile deleted successfully", null));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting profile", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getProfiles(HttpServletRequest servletRequest) {
        try {
            List<ProfileDto> profileDtos = profileService.getProfiles();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Fetched profiles list");
            return ResponseEntity.ok(new ApiResponse("Fetched profiles list:", profileDtos));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting profiles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting profiles", e.getMessage()));
        }
    }


    @GetMapping("/listId/{profileId}")
    public ResponseEntity<ApiResponse> getProfileById(HttpServletRequest servletRequest, @PathVariable("profileId") Integer id) {
        try {
            ProfileDto profileDto = profileService.getProfileById(id); // Cambiado aquí
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Profile found with ID:" + id);
            return ResponseEntity.ok(new ApiResponse("Profile successfully obtained", profileDto));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting profile", e.getMessage()));
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<ApiResponse> searchName(HttpServletRequest servletRequest, @RequestParam String name){
        try {
           List<ProfileDto> nameProfile = profileService.searchName(name);
           Utilities.infoLog(servletRequest, HttpStatus.OK,"Profile found with the name: " + name);
           return ResponseEntity.ok(new ApiResponse("Profile successfully obtained", nameProfile));
        } catch (Exception e) {
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting profile", e.getMessage()));
        }
    }
}
