package dev.hkb.ananta.controller;

import dev.hkb.ananta.dto.jwt.JwtResponse;
import dev.hkb.ananta.dto.users.CreateUserRequest;
import dev.hkb.ananta.dto.users.LoginDTO;
import dev.hkb.ananta.dto.users.UserResponse;
import dev.hkb.ananta.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Valid @RequestBody CreateUserRequest userDto){
        UserResponse uto =  userService.save(userDto);
        return new ResponseEntity<>(uto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody LoginDTO loginDTO){
        String token = userService.verifyUser(loginDTO);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
