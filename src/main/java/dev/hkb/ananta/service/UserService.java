package dev.hkb.ananta.service;

import dev.hkb.ananta.dto.users.CreateUserRequest;
import dev.hkb.ananta.dto.users.UserResponse;

public interface UserService {
    UserResponse save(CreateUserRequest userDto);
}
