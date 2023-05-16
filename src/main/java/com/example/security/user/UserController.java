package com.example.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/Settings")
public class UserController {
    private final UserService userService;
    @Autowired //so constructor will work
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/MyAccount")
    public UserProfile getUser(@RequestHeader(value="authorization") String auth){
        return userService.getUserFromAuth(auth);
    }
    @PostMapping("/EditAccount")
    public void editUser(@RequestHeader(value="authorization") String auth, @RequestBody UserProfile up){
        userService.editUser(auth, up);
    }
}
