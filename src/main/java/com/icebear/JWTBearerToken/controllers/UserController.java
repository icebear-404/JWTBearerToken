package com.icebear.JWTBearerToken.controllers;

import java.util.List;
import java.util.Optional;

import java.util.HashMap; 
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.icebear.JWTBearerToken.services.JwtService; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icebear.JWTBearerToken.dtos.UserDto;
import com.icebear.JWTBearerToken.services.UserService;

@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService ) {
        this.userService = userService;
        this.jwtService = jwtService; 
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> authenticatedUser() {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder
          .getContext()
          .getAuthentication();

        // Retrieve the current user details (UserDetails) from the authentication object
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        // Generate a JWT token for the current user
        String token = jwtService.generateToken(currentUser);

        // Prepare the response containing the user details and the token
        Map<String, Object> response = new HashMap<>();
        response.put("user", currentUser);
        response.put("token", token);

        // Return the response with HTTP 200 OK status
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
    Optional<UserDto> userDto = userService.findByIdAsDto(id);
        return userDto
          .map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAllAsDto();
        return ResponseEntity.ok(users);
    }

}
    
