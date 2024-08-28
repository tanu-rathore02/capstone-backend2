package com.backend.lms.controller;

import com.backend.lms.model.Users;
import com.backend.lms.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/me")
    public ResponseEntity<Users> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Users)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Users currentUser = (Users) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = usersService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Integer id) {
        try {
            Users users = usersService.getUserById(id);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<Users>> getUsersByRole(@PathVariable String role) {
        List<Users> users = usersService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users createdUser = usersService.createUser(user);
        return ResponseEntity.status(201).body(createdUser); // Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Integer id, @RequestBody Users userDetails) {
        try {
            Users updatedUser = usersService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        try {
            usersService.deleteUser(id);
            return ResponseEntity.noContent().build(); // No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // Not Found
        }
    }
}
