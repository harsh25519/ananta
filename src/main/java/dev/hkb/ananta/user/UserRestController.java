package dev.hkb.ananta.user;

import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.user.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    ///  Get User Profile only for Customers
    @GetMapping("/me")
    public ResponseEntity<?> showProfile(@AuthenticationPrincipal UserPrincipal principal){
        UserResponse user = userService.getCurrentUser(principal.getUsername());
        return ResponseEntity.ok(user);
    }

    /// Admin can access the whole list of users.
    @GetMapping
    public ResponseEntity<?> showUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /// Delete Mapping for deletion of user
    /// But cannot delete a user physically as it will create problem for Past Order Records
    /// For this we can use """isActive"""
//    @DeleteMapping("/delete")
//    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserPrincipal principal){
//        userService.deleteCurrentUser(principal.getUsername());
//        return ResponseEntity.noContent().build();
//    }

}
