package com.is4tech.base.service;

import com.is4tech.base.domain.Profile;
import com.is4tech.base.domain.ProfileHasRole;
import com.is4tech.base.domain.ProfileRoleId;
import com.is4tech.base.domain.Roles;
import com.is4tech.base.dto.ProfileHasRoleCreateDto;
import com.is4tech.base.dto.ProfileHasRoleDTO;
import com.is4tech.base.repository.ProfileHasRoleRepository;
import com.is4tech.base.repository.ProfileRepository;
import com.is4tech.base.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProfileHasRoleService {


    private final ProfileHasRoleRepository profileHasRoleRepository;
    private final RolesRepository rolesRepository;
    private final ProfileRepository profileRepository;

    public ProfileHasRoleDTO createProfileHasRole(ProfileHasRoleCreateDto dto) {
        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Roles role = rolesRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        ProfileHasRole profileHasRole = new ProfileHasRole();
        ProfileRoleId profileRoleId = new ProfileRoleId(dto.getProfileId(), dto.getRoleId());

        profileHasRole.setProfileRoleId(profileRoleId);
        profileHasRole.setProfile(profile);
        profileHasRole.setRole(role);

        ProfileHasRole savedProfileHasRole = profileHasRoleRepository.save(profileHasRole);

        return new ProfileHasRoleDTO(
                savedProfileHasRole.getProfile().getProfileId(),
                savedProfileHasRole.getProfile().getName(),
                savedProfileHasRole.getRole().getRoleId(),
                savedProfileHasRole.getRole().getName()
        );
    }

}
