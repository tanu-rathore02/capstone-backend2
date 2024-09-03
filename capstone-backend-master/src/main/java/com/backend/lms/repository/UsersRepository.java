package com.backend.lms.repository;

import com.backend.lms.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Page<Users> findByMobileNumberContainingIgnoreCaseAndRole(String mobile, String role, Pageable pageable);
    Page<Users> findByRole(String role, Pageable pageable);
    List<Users> findByRole(String role, Sort sort);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByMobileNumber(String mobileNumber);

}