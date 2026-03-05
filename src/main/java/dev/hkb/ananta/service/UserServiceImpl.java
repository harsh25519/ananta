package dev.hkb.ananta.service;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.dao.UserRepository;
import dev.hkb.ananta.dto.users.CreateUserRequest;
import dev.hkb.ananta.dto.users.LoginDTO;
import dev.hkb.ananta.dto.users.UserResponse;
import dev.hkb.ananta.entity.Users;
import dev.hkb.ananta.mapper.UserMapper;
import dev.hkb.ananta.utils.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final JwtUtilService jwtUtilService;
    private final AuthenticationManager authenticationManager;
    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder,
                           JwtUtilService jwtUtilService, AuthenticationManager authenticationManager,
                           UserMapper userMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder  = passwordEncoder;
        this.jwtUtilService = jwtUtilService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getCurrentUser(String email) {
        return userRepo.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database"));
    }

    @Override
    public String verifyUser(LoginDTO loginDTO){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.email(),
                        loginDTO.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
//        Optional<Users> user = userRepo.findByEmail(loginDTO.email());
//
//        if(user.isEmpty()){
//            throw new UserNotFoundException("User does not exist with this email.");
//        }
//
//        Users ur = user.get();
//        if(!loginDTO.password().equals(ur.getPassword())){
//            throw new UserNotAuthenticated("Password is incorrect. Please! try again.");
//        }

        String token = jwtUtilService.generateToken(loginDTO.email());
        return token;
    }


    @Override
    @Transactional
    public UserResponse save(CreateUserRequest userDto) {
        Users user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRole(UserRoles.CUSTOMER);
        Users u = userRepo.save(user);
        UserResponse ur = userMapper.toDto(u);
        return ur;
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<Users> users = userRepo.findAll();
        List<UserResponse> ur = users.stream()
                                    .map(userMapper::toDto)
                                    .toList();
        return ur;
    }

    @Override
    @Transactional
    public void deleteCurrentUser(String email) {
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found in database"));
        userRepo.delete(user);
    }

}
