package com.is4tech.base.repository;

import com.is4tech.base.domain.ProfileHasRole;
import com.is4tech.base.domain.ProfileRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileHasRoleRepository extends JpaRepository<ProfileHasRole, ProfileRoleId> {
}
