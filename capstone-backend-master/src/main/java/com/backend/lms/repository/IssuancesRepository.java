package com.backend.lms.repository;

import com.backend.lms.model.Issuances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuancesRepository extends JpaRepository<Issuances, Integer> {
}
