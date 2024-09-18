package com.backend.lms.repository;

import com.backend.lms.model.Issuances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IssuancesRepository extends JpaRepository<Issuances, Long> {

    @Query("SELECT DISTINCT i FROM Issuances i " +
            "LEFT JOIN i.books b " +
            "LEFT JOIN i.users u " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.mobileNumber) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Issuances> findByBooksOrUsers(@Param("search") String search, Pageable pageable);


    List<Issuances> findByUsersId(Long userId);

    List<Issuances> findByBooks_Id(Long bookId);

    boolean existsByBooksIdAndStatus(Long bookId, String status);

    boolean existsByUsersIdAndStatus(Long userId, String status);

    List<Issuances> findAllByReturnDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, String status);

//    @Query("SELECT COUNT(u) FROM Issuances u WHERE u.issuanceType = 'IN-HOUSE' GROUP BY u.users.id")
    @Query("SELECT COUNT(DISTINCT u.users.id) FROM Issuances u WHERE u.issuanceType = 'IN-HOUSE'")
    Long countInHouse();

    // Query to find all by returnDate between two dates and status "Issued"
    @Query("SELECT i FROM Issuances i WHERE i.returnDate BETWEEN :startOfTomorrow AND :endOfTomorrow AND i.status = :status")
    List<Issuances> findAllByExpectedReturnTimeBetweenAndStatus(
            @Param("startOfTomorrow") LocalDateTime startOfTomorrow,
            @Param("endOfTomorrow") LocalDateTime endOfTomorrow,
            @Param("status") String status
    );
}
