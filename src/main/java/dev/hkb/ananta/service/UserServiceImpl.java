package dev.hkb.ananta.service;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.dao.UserRepository;
import dev.hkb.ananta.dto.users.CreateUserRequest;
import dev.hkb.ananta.dto.users.UserResponse;
import dev.hkb.ananta.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder  = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponse save(CreateUserRequest userDto) {
        Users user = toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        Users u = userRepo.save(user);
        UserResponse ur = toDto(u);
        return ur;
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<Users> users = userRepo.findAll();
        List<UserResponse> ur = new ArrayList<>();
        for(Users Urp : users){
            var e = toDto(Urp);
            ur.add(e);
        }

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
