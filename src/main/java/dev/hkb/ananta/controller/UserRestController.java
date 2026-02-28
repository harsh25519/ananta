package dev.hkb.ananta.controller;

import dev.hkb.ananta.dto.users.CreateUserRequest;
import dev.hkb.ananta.dto.users.UserResponse;
import dev.hkb.ananta.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserResponse addUser(@Valid @RequestBody CreateUserRequest userDto){
        return userService.save(userDto);
    }

    @GetMapping("/getList")
    public List<UserResponse> showUser(){
        return userService.getAllUsers();
    }

}
