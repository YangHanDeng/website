package com.example.security.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User getUserById(Integer id){
        var user = userRepository.findById(id).orElseThrow();
        return user;
    }

    public User getUserByEmail(String email){
        var user = userRepository.findUserByEmail(email).orElseThrow();
        return user;
    }

    private String extractEmailFromAuth(String auth){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = auth.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        JSONObject json = new JSONObject(payload);
        return json.getString("sub");
    }

    public UserProfile getUserFromAuth(String auth) {
        String email = extractEmailFromAuth(auth);
        User u = getUserByEmail(email);
        UserProfile up = new UserProfile(u.getUsername(),u.getRealName(),u.getEmail(),u.getAge(),u.getAddress(),u.getPhone());
        return up;
    }

    @Transactional
    public void editUser(String auth, UserProfile up) {
        String email = extractEmailFromAuth(auth);
        User u = getUserByEmail(email);
        if(up.getUsername() != null && up.getUsername() != u.getUsername()){
            u.setUsername(up.getUsername());
        }
        if(up.getRealName() != null && up.getRealName() != u.getRealName()){
            u.setRealName(up.getRealName());
        }
        if(up.getEmail() != null && up.getEmail() != u.getEmail()){
            u.setEmail(up.getEmail());
        }
        if(up.getAge() != null && up.getAge() != u.getAge()){
            u.setAge(up.getAge());
        }
        if(up.getAddress() != null && up.getAddress() != u.getAddress()){
            u.setAddress(up.getAddress());
        }
        if(up.getPhone() != null && up.getPhone() != u.getPhone()){
            u.setPhone(up.getPhone());
        }
    }

}
