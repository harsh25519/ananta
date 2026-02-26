package dev.hkb.ananta.controller;

import dev.hkb.ananta.requestDTO.CreateUserRequest;
import dev.hkb.ananta.responseDTO.UserResponse;
import dev.hkb.ananta.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/post")
    public UserResponse addUser(@Valid @RequestBody CreateUserRequest userDto){
        return userService.save(userDto);
    }
}
