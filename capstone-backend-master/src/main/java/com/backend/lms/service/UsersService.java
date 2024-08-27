package com.backend.lms.service;

import com.backend.lms.model.Users;
import com.backend.lms.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // Method to get all users
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    // Method to get a user by ID
    public Users getUserById(Integer id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // Method to get users by role
    public List<Users> getUsersByRole(String role) {
        return usersRepository.findByRole(role);
    }

    // Method to create a new user
    public Users createUser(Users users) {
        return usersRepository.save(users);
    }

    // Method to update an existing user
    public Users updateUser(Integer id, Users userDetails) {
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        existingUser.setFullName(userDetails.getFullName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPhoneNo(userDetails.getPhoneNo());
        existingUser.setPassword(userDetails.getPassword()); // Note: You might want to handle password encoding separately
        existingUser.setRole(userDetails.getRole());

        return usersRepository.save(existingUser);
    }

    // Method to delete a user
    public void deleteUser(Integer id) {
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        usersRepository.delete(existingUser);
    }
}
