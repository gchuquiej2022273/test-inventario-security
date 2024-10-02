package com.is4tech.base.repository;

import com.is4tech.base.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    boolean existsByName(String name);
    List<Profile> findByName(String name);
}
