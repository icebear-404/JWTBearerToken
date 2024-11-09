package com.icebear.JWTBearerToken.services;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.icebear.JWTBearerToken.dtos.LoginUserDto;
import com.icebear.JWTBearerToken.dtos.RegisterUserDto;
import com.icebear.JWTBearerToken.entities.User;
import com.icebear.JWTBearerToken.repositories.UserRepository;

import jakarta.validation.Valid;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder
    ) {
      this.userRepository = userRepository;
      this.authenticationManager = authenticationManager;
      this.passwordEncoder = passwordEncoder;
    }

    public User signup(@Valid RegisterUserDto input) {
        
        if (userRepository.findByEmail(input.getEmail()).isPresent()) {
          throw new IllegalArgumentException(
            "User already exists with email: " + input.getEmail()
          );
        }

        String hashedPassword = passwordEncoder.encode(input.getPassword());
        User user = new User();
        user.setEmail(input.getEmail());
        user.setPassword(hashedPassword);
        user.setFullName(input.getFullName());

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        User user = userRepository
          .findByEmail(input.getEmail())
          .orElseThrow(() ->
            new IllegalArgumentException("Email not found: " + input.getEmail())
          );

        
        if (!passwordEncoder.matches(input.getPassword(), user.getPassword())) {
          throw new IllegalArgumentException(
            "Incorrect password for email: " + input.getEmail()
          );
        }

        
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
            input.getEmail(),
            input.getPassword()
          )
        );

        return user; 
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

}
