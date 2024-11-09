package com.icebear.JWTBearerToken.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(); 
    }

    @Override
    public String getUsername() {
      return email; 
    }

    @Override
    public boolean isAccountNonExpired() {
      return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
      return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true; 
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
}
