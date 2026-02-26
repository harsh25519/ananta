package dev.hkb.ananta.service;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.dao.UserRepository;
import dev.hkb.ananta.entity.Users;
import dev.hkb.ananta.requestDTO.CreateUserRequest;
import dev.hkb.ananta.responseDTO.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepo;

    @Override
    @Transactional
    public UserResponse save(CreateUserRequest userDto) {
        Users user = toEntity(userDto);
        Users u = userRepo.save(user);
        UserResponse ur = toDto(u);
        return ur;
    }

    public Users toEntity(CreateUserRequest userDto){
        return new Users(userDto.firstName(), userDto.lastName(), userDto.email(), userDto.password(),
                userDto.phoneNumber(), UserRoles.CUSTOMER);
    }

    public UserResponse toDto(Users user){
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPhoneNumber(), user.getRole(), user.getCreatedAt());
    }
}
