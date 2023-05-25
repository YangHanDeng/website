package com.example.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    //fields are same as /user/User
    private String username;
    private String realname;
    private String email;
    private String password;
    private Integer age;
    private String address;
    private String phone;
}
