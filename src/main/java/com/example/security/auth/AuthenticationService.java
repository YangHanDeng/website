package com.example.security.auth;

import com.example.security.config.JwtService;
import com.example.security.user.Role;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//implement register and auth
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder pwdencoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) throws IllegalStateException {
        //check if email is used
        userRepository.findByEmail(request.getEmail()).ifPresent(s -> {
            throw new IllegalStateException("email used");
        });
        //create user
        var user = User.builder().username(request.getUsername())
                .realName(request.getRealName())
                .email(request.getEmail())
                .password(pwdencoder.encode(request.getPassword()))//encode pwd bf save 2 db
                .age(request.getAge())
                .address(request.getAddress())
                .phone(request.getPhone())
                .role(Role.USER)
                .build();
        //save to db
        userRepository.save(user);
        //return generation token
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // if uname/pwd invalid, exception will be thrown
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        //both correct
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
