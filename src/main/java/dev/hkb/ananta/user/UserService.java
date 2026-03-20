package dev.hkb.ananta.user;

import dev.hkb.ananta.user.dto.CreateUserRequest;
import dev.hkb.ananta.user.dto.LoginDTO;
import dev.hkb.ananta.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse save(CreateUserRequest userDto);

    List<UserResponse> getAllUsers();

    UserResponse getCurrentUser(String email);

    void deleteCurrentUser(String email);

    public String verifyUser(LoginDTO loginDTO);
}
