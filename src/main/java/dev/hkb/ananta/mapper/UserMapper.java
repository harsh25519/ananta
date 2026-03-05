package dev.hkb.ananta.mapper;

import dev.hkb.ananta.dto.users.CreateUserRequest;
import dev.hkb.ananta.dto.users.UserResponse;
import dev.hkb.ananta.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserResponse toDto(Users user);

    Users toEntity(CreateUserRequest userDto);
}
