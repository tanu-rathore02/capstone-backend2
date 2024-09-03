package com.backend.lms.repository;

import com.backend.lms.model.Issuances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuancesRepository extends JpaRepository<Issuances, Long> {

    Page<Issuances> findByBooks_TitleContainingIgnoreCase(String title, Pageable pageable);


    List<Issuances> findByUsersId(Long userId);

    List<Issuances> findByBooksId(Long bookId);
}

