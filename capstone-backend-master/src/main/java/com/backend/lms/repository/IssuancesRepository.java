package com.backend.lms.repository;

import com.backend.lms.model.Issuances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IssuancesRepository extends JpaRepository<Issuances, Long> {

    Page<Issuances> findByBooks_TitleContainingIgnoreCase(String title, Pageable pageable);


    List<Issuances> findByUsersId(Long userId);

    List<Issuances> findByBooksId(Long bookId);

//    @Query("SELECT i FROM Issuances i WHERE i.user.id = :id")
//    List<Issuances> findAllByUserId(Long id);

//    @Query("SELECT i FROM Issuances i WHERE i.user.mobileNumber = :mobileNumber")
//    List<Issuances> findAllByUserMobile(String mobileNumber);
}

