package dev.hkb.ananta.controller;

import dev.hkb.ananta.dto.users.CreateUserRequest;
import dev.hkb.ananta.dto.users.UserResponse;
import dev.hkb.ananta.service.UserService;
import dev.hkb.ananta.utils.JwtUtilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private final JwtUtilService jwtUtilService;
    private UserService userService;

    @Autowired
    public UserRestController(UserService userService, JwtUtilService jwtUtilService) {
        this.userService = userService;
        this.jwtUtilService = jwtUtilService;
    }

    @PostMapping("/signup")
    public String addUser(@Valid @RequestBody CreateUserRequest userDto){
        UserResponse uto =  userService.save(userDto);
        return jwtUtilService.generateToken(uto.email());
    }

    @GetMapping("/getList")
    public List<UserResponse> showUser(){
        return userService.getAllUsers();
    }

}
