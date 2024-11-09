package com.icebear.JWTBearerToken.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icebear.JWTBearerToken.dtos.LoginResponseDto;
import com.icebear.JWTBearerToken.dtos.LoginUserDto;
import com.icebear.JWTBearerToken.dtos.RegisterUserDto;
import com.icebear.JWTBearerToken.entities.User;
import com.icebear.JWTBearerToken.services.AuthenticationService;
import com.icebear.JWTBearerToken.services.JwtService;

import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;


    public AuthenticationController(
        AuthenticationService authenticationService, 
        JwtService jwtService
    ) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(
        @Valid @RequestBody RegisterUserDto registerUserDto
    ) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

     @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(
      @RequestBody LoginUserDto loginUserDto
    ) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        
        
        String jwtToken = jwtService.generateToken(authenticatedUser); 
        LoginResponseDto loginResponse = new LoginResponseDto()
          .setToken(jwtToken)
          .setExpiresIn(jwtService.getExpirationTime()
        );
        
        return ResponseEntity.ok(loginResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex
          .getBindingResult()
          .getAllErrors()
          .forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
          });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    
}
