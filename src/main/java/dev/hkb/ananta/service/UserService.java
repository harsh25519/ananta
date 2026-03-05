package dev.hkb.ananta.service;

import dev.hkb.ananta.dto.users.CreateUserRequest;
import dev.hkb.ananta.dto.users.LoginDTO;
import dev.hkb.ananta.dto.users.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse save(CreateUserRequest userDto);

    List<UserResponse> getAllUsers();

    UserResponse getCurrentUser(String email);

    void deleteCurrentUser(String email);

    public String verifyUser(LoginDTO loginDTO);
}
