package com.backend.lms.repository;

import com.backend.lms.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByPhoneNo(Long phoneNo);
    List<Users> findByRole(String role);
}