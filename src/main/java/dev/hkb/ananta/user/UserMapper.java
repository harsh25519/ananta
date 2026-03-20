package dev.hkb.ananta.user;

import dev.hkb.ananta.user.dto.CreateUserRequest;
import dev.hkb.ananta.user.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserResponse toDto(Users user);

    Users toEntity(CreateUserRequest userDto);
}
