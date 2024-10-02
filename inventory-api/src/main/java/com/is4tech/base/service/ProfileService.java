package com.is4tech.base.service;

import com.is4tech.base.domain.Profile;
import com.is4tech.base.domain.ProfilesRoles;
import com.is4tech.base.domain.ProfilesRolesId;
import com.is4tech.base.domain.Roles;
import com.is4tech.base.dto.ProfileDto;
import com.is4tech.base.exception.Exceptions;
import com.is4tech.base.repository.ProfileRepository;
import com.is4tech.base.repository.ProfilesRolesRepository;
import com.is4tech.base.repository.RolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private RolesRepository roleRepository;
    @Autowired
    private ProfilesRolesRepository profilesRolesRepository;

    public Profile saveProfile(ProfileDto input) {
        validateProfileDto(input);

        Profile profile = new Profile();
        profile.setName(input.getName());
        profile.setDescription(input.getDescription());
        profile.setStatus(true);

        Profile savedProfile = profileRepository.save(profile);

        //convertimos los id a nombres
        List<Integer> roleIds = input.getResource().stream()
                //mapeamos y buscamos el rol por nombre
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new Exceptions("Role not found with name: " + roleName))
                        .getRoleId())
                .collect(Collectors.toList());

        assignRolesToProfile(savedProfile.getProfileId(), roleIds);

        return savedProfile;
    }

    @Transactional
    public Profile updateProfile(ProfileDto input, Integer id) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Profile not found"));

        validateProfileDto(input);

        existingProfile.setName(input.getName());
        existingProfile.setDescription(input.getDescription());
        if (input.getStatus() != null) {
            existingProfile.setStatus(input.getStatus());
        }

        //Asignamos roles al perfil usando el id de roles actualizados
        List<Integer> roleIds = input.getResource().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new Exceptions("Role not found with name: " + roleName))
                        .getRoleId())
                .collect(Collectors.toList());

        assignRolesToProfile(existingProfile.getProfileId(), roleIds); //Asignar roles al perfil actualizado
        return profileRepository.save(existingProfile); // Guardar y retornar el perfil actualizado
    }

    @Transactional
    public void deleteProfile(Integer id) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Profile not found"));

        //Elimina los roles asociados antes de eliminar el perfil
        profilesRolesRepository.deleteByProfile_ProfileId(existingProfile.getProfileId());
        profileRepository.delete(existingProfile);
    }

    public List<ProfileDto> getProfiles() {
        List<Profile> profiles = profileRepository.findAll();

        if (profiles.isEmpty()) {
            throw new Exceptions("No Profiles found");
        }
        // convertimos cada Profile a ProfileDto incluyendo los nombres de roles
        return profiles.stream()
                .map(this::convertToDto) // convertimos Profile a ProfileDto
                .collect(Collectors.toList());
    }

    private ProfileDto convertToDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        BeanUtils.copyProperties(profile, profileDto); //copiamos propiedades de Profile a ProfileDto

        //recuperamos los nombres de roles asociados
        List<Roles> roles = profilesRolesRepository.findRoles(profile.getProfileId());
        List<String> roleNames = roles.stream()
                .map(Roles::getName) //extrasemos los nombres de los roles
                .collect(Collectors.toList());

        //establecemos los nombres de los roles en el DTO
        profileDto.setResource(roleNames);

        return profileDto;
    }

    public ProfileDto getProfileById(Integer id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new Exceptions("Profile not found with id: " + id));

        ProfileDto profileDto = new ProfileDto();
        BeanUtils.copyProperties(profile, profileDto);

        //recuperamos los nombres de roles asociados
        List<Roles> roles = profilesRolesRepository.findRoles(id);
        List<String> resource = roles.stream()
                .map(Roles::getName) //extraemos los nombres de los roles
                .collect(Collectors.toList());

        profileDto.setResource(resource);
        return profileDto;
    }

    //Creamos un metodo para asignar un rol a perfil
    private void assignRolesToProfile(Integer profileId, List<Integer> roleIds) {
        profilesRolesRepository.deleteByProfile_ProfileId(profileId);

        if (roleIds != null) {
            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new Exceptions("Profile not found with id: " + profileId));

            //asignamos nuevos roles
            for (Integer roleId : roleIds) {
                Roles role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new Exceptions("Role not found with id: " + roleId));

                ProfilesRolesId profilesRolesId = new ProfilesRolesId(profileId, roleId);
                ProfilesRoles profilesRoles = new ProfilesRoles(profilesRolesId, profile, role);
                profilesRolesRepository.save(profilesRoles);
            }
        }
    }

    public List<ProfileDto> searchName(String name) {
        List<Profile> nameProfile = profileRepository.findByName(name);

        if(nameProfile.isEmpty()){
            throw new Exceptions("Profile name not found: " + name);
        }
        return nameProfile.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private void validateProfileDto(ProfileDto input) {
        if (input.getName() == null || input.getName().trim().isEmpty()) {
            throw new Exceptions("Name cannot be empty");
        }
        if (input.getDescription() == null || input.getDescription().trim().isEmpty()) {
            throw new Exceptions("Description cannot be empty");
        }
        if (profileRepository.existsByName(input.getName())) {
            throw new Exceptions("The name already exists");
        }
    }
}
