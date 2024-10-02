package com.is4tech.base.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles_has_profile")
@Data
public class ProfileHasRole {

    @EmbeddedId
    private ProfileRoleId profileRoleId;

    @ManyToOne
    @MapsId("profileId")
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Roles role;
}
