package com.example.security.user;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String firstname;
    private String lastname;
    private String email;
    private Integer age;
    private String address;
    private String phone;
}
