package dev.hkb.ananta.controller;

import dev.hkb.ananta.dto.users.UserResponse;
import dev.hkb.ananta.service.UserService;
import dev.hkb.ananta.utils.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<?> showProfile(@AuthenticationPrincipal UserPrincipal principal){
        UserResponse user = userService.getCurrentUser(principal.getUsername());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getList")
    public ResponseEntity<?> showUser(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserPrincipal principal){
        userService.deleteCurrentUser(principal.getUsername());
        return ResponseEntity.noContent().build();
    }

}
