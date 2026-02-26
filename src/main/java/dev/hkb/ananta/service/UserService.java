package dev.hkb.ananta.service;

import dev.hkb.ananta.requestDTO.CreateUserRequest;
import dev.hkb.ananta.responseDTO.UserResponse;

public interface UserService {
    UserResponse save(CreateUserRequest userDto);
}
