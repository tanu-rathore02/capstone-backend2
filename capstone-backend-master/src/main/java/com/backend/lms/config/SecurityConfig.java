package com.backend.lms.config;


import com.backend.lms.exception.CustomBasicAuthenticationEntryPoint;
import com.backend.lms.exception.CustomAccessDeniedHandler;
import com.backend.lms.filter.JWTTokenValidatorFilter;
import com.backend.lms.Provider.CustomUsernamePwdAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

//        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure());  // Only HTTP

        http
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrfConfig -> csrfConfig.disable())
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000/"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);

                        return config;
                    }
                }));

        http.authorizeHttpRequests((requests) -> requests
//                Other routes
                        .requestMatchers("/api/login", "/api/error").permitAll()
                        .requestMatchers("/api/register").hasRole("ADMIN")
                        .requestMatchers("/api/current-user").authenticated()

//                Category routes
                        .requestMatchers("/api/allCategories", "/api/getCategory/{id}", "/api/searchCategories").permitAll()
                        .requestMatchers("/api/createCategory", "/api/updateCategory/{id}", "/api/deleteCategory/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()

//                Book routes
                        .requestMatchers( "/api/allBooks","/api/getBooks/{id}").hasRole("ADMIN")
                        .requestMatchers( "/api/createBooks", "/api/updateBooks/{id}").hasRole("ADMIN")
                        .requestMatchers( "/api/deleteBooks/{id}").hasRole("ADMIN")



//                User routes
                        .requestMatchers( "/api/user/**").hasRole("ADMIN")
                        .requestMatchers( "/api/users").hasRole("ADMIN")
                        .requestMatchers( "/api/user-count").hasRole("ADMIN")
                        .requestMatchers( HttpMethod.GET, "/api/user/**").hasAnyRole("ADMIN", "USER")

//                Issuance routes
                        .requestMatchers("/api/allIssuances", "/getIssuance/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/issuances").authenticated()
                        .requestMatchers("/api/createIssuance", "api/updateIssuance/{id}", "api/deleteIssuance?{id}").hasRole("ADMIN")
                        .requestMatchers( HttpMethod.GET, "/api/issuance/**").hasAnyRole("ADMIN", "USER")


//                        .requestMatchers("/**").permitAll()

        );

        http
//                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class);

        http.formLogin(formLoginConfig -> formLoginConfig.disable());

        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // by default it is using bcrypt password encoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        CustomUsernamePwdAuthenticationProvider authenticationProvider = new CustomUsernamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }

}