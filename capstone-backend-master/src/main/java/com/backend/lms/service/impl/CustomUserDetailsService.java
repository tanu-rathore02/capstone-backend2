package com.backend.lms.service.impl;

import com.backend.lms.model.Users;
import com.backend.lms.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.contains("@")) {

            Users user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User details not found for " + username));
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

        } else {

            Users user = userRepository.findByMobileNumber(username).orElseThrow(() -> new UsernameNotFoundException("User details not found for " + username));
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));

            return new org.springframework.security.core.userdetails.User(user.getMobileNumber(), user.getPassword(), authorities);
        }


    }
}