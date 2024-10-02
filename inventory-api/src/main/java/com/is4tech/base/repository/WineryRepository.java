package com.is4tech.base.repository;

import com.is4tech.base.domain.Winery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WineryRepository extends JpaRepository<Winery, Integer> {
}
