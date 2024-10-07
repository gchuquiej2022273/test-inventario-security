package com.is4tech.base.controller;

import com.is4tech.base.domain.Profile;
import com.is4tech.base.dto.ApiResponse;
import com.is4tech.base.dto.ProfileDto;
import com.is4tech.base.service.AuditService;
import com.is4tech.base.service.ProfileService;
import com.is4tech.base.util.Utilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AuditService auditService;
    private static final String ENTIDAD = "Profile";

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createProfile(HttpServletRequest servletRequest, HttpServletResponse response, @RequestBody ProfileDto profileDto) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD,String.valueOf(profileDto) ,"Profile created successfully");
            Profile createdProfile = profileService.saveProfile(profileDto);
            Utilities.infoLog(servletRequest, HttpStatus.CREATED, "Profile created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Profile added successfully", createdProfile));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD,String.valueOf(profileDto) ,"Error created profile");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error adding profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error adding a profile", e.getMessage()));
        }
    }

    @PutMapping("/update/{profileId}")
    public ResponseEntity<ApiResponse> updateProfile(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("profileId") Integer id, @RequestBody ProfileDto profile) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(profile),"Profile updated successfully by id");
            Profile updatedProfile = profileService.updateProfile(profile, id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Profile updated successfully");
            return ResponseEntity.ok(new ApiResponse("Profile updated successfully", updatedProfile));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(profile),"Error updating profile by id");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error updating profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating profile", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{profileId}")
    public ResponseEntity<ApiResponse> deleteProfile(HttpServletRequest servletRequest, HttpServletResponse response,@PathVariable("profileId") Integer id) {
        try {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(id),"Profile deleted successfully by Id");
            profileService.deleteProfile(id);
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Profile deleted successfully");
            return ResponseEntity.ok(new ApiResponse("Profile deleted successfully", null));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, String.valueOf(id),"Error deleting profile by id");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting profile", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getProfiles(HttpServletRequest servletRequest, HttpServletResponse response) {
        try {
            List<ProfileDto> profileDtos = profileService.getProfiles();
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Fetched profiles list");
            return ResponseEntity.ok(new ApiResponse("Fetched profiles list:", profileDtos));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, "ErrorGetProfile","Error getting profiles");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting profiles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting list of profiles", e.getMessage()));
        }
    }


    @GetMapping("/listId/{profileId}")
    public ResponseEntity<ApiResponse> getProfileById(HttpServletResponse response,HttpServletRequest servletRequest, @PathVariable("profileId") Integer id) {
        try {
            ProfileDto profileDto = profileService.getProfileById(id); // Cambiado aquí
            Utilities.infoLog(servletRequest, HttpStatus.OK, "Profile found with ID:" + id);
            return ResponseEntity.ok(new ApiResponse("Profile successfully obtained", profileDto));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, "ErrorGetProfile","Error getting profiles by id");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting list of profiles", e.getMessage()));
        }
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<ApiResponse> searchName(HttpServletResponse response,HttpServletRequest servletRequest, @RequestParam("name") String name){
        try {
           List<ProfileDto> nameProfile = profileService.searchName(name);
           Utilities.infoLog(servletRequest, HttpStatus.OK,"Profile found with the name: " + name);
           return ResponseEntity.ok(new ApiResponse("Profile successfully obtained", nameProfile));
        } catch (Exception e) {
            auditService.createAudit(servletRequest, response, ENTIDAD, "ErrorGetProfileName","Error getting profiles by name");
            Utilities.errorLog(servletRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Error getting profile", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error getting list of profiles by name", e.getMessage()));
        }
    }
}
