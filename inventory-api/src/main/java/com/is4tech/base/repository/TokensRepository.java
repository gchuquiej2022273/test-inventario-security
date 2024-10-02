package com.is4tech.base.repository;

import com.is4tech.base.domain.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<Tokens, Integer> {
    Optional<Tokens> findByToken(String token);
}
