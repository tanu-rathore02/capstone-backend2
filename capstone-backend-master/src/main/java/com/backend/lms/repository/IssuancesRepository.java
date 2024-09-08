package com.backend.lms.repository;

import com.backend.lms.model.Books;
import com.backend.lms.model.Issuances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuancesRepository extends JpaRepository<Issuances, Long> {

    Page<Issuances> findByBooks_TitleContainingIgnoreCase(String title, Pageable pageable);

    List<Issuances> findByUsersId(Long userId);

    List<Issuances> findByBooks_Id(Long bookId);

    boolean existsByBooks_IdAndStatus(Long bookId, String status);

    boolean existsByUsersIdAndStatus(Long userId, String status);

    @Query("SELECT COUNT(u) FROM Issuances u WHERE u.issuanceType = 'IN-HOUSE'")
    long count();
}
