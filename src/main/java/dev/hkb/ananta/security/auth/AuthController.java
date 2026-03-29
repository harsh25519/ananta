package dev.hkb.ananta.security.auth;

import dev.hkb.ananta.security.jwt.JwtResponse;
import dev.hkb.ananta.user.dto.CreateUserRequest;
import dev.hkb.ananta.user.dto.LoginDTO;
import dev.hkb.ananta.user.dto.UserResponse;
import dev.hkb.ananta.user.UserService;
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

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /// First time signup available for all
    /// Any person can apply for sign up
    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Valid @RequestBody CreateUserRequest userDto){
        UserResponse uto =  userService.save(userDto);
        return new ResponseEntity<>(uto, HttpStatus.CREATED);
    }

    /// login for jwt service
    /// This to login after user is signed in.
    /// After login it will receive a Jwt token in response to login again for a certain period of time
    @PostMapping("/login")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody LoginDTO loginDTO){
        String token = userService.verifyUser(loginDTO);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
