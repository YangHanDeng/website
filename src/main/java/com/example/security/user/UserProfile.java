package com.example.security.user;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @NotBlank
    private String username;
    @NotBlank
    private String realName;
    @NotBlank
    private String email;
    @NotBlank
    private Integer age;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
}
