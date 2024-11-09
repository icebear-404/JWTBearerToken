package com.icebear.JWTBearerToken.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.icebear.JWTBearerToken.dtos.UserDto;
import com.icebear.JWTBearerToken.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> findAllAsDto() {
        return userRepository
          .findAll()
          .stream()
          .map(user ->
            new UserDto(user.getId(), user.getFullName(), user.getEmail())
          )
          .collect(Collectors.toList());
    }

    public Optional<UserDto> findByIdAsDto(Long id) {
        return userRepository
          .findById(id)
          .map(user ->
            new UserDto(user.getId(), user.getFullName(), user.getEmail())
          );
    }

    
}
