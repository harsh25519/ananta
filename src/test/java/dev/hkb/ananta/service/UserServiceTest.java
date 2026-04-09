package dev.hkb.ananta.service;

import dev.hkb.ananta.address.AddressRepository;
import dev.hkb.ananta.cart.CartRepository;
import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.exceptionHandler.UserNotFound;
import dev.hkb.ananta.security.jwt.JwtUtilService;
import dev.hkb.ananta.security.utils.EmailService;
import dev.hkb.ananta.user.UserMapper;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.UserServiceImpl;
import dev.hkb.ananta.user.Users;
import dev.hkb.ananta.user.dto.CreateUserRequest;
import dev.hkb.ananta.user.dto.LoginDTO;
import dev.hkb.ananta.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepo;
    @Mock
    private EmailService emailService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtilService jwtUtilService;

    @Test
    void save_ShouldEncodePasswordAndSendEmail(){
        CreateUserRequest user = new CreateUserRequest("test","tester","test@test.com",
                "rawPass","9548136572");
        Users mockUser = new Users();
        mockUser.setEmail(user.email());

        when(userMapper.toEntity(any())).thenReturn(mockUser);
        when(passwordEncoder.encode("rawPass")).thenReturn("hashedPassword");
        when(userRepo.save(any())).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(new UserResponse(Long.parseLong("7"),"hfs","sign",
                "onfg@gm.com","9465813579", UserRoles.CUSTOMER, OffsetDateTime.now()));

        userService.save(user);

        verify(passwordEncoder).encode("rawPass");
        verify(userRepo).save(argThat(us -> us.getPassword().equals("hashedPassword")));
        verify(emailService).sendMailToNewUser("test@test.com", "rawPass");
    }

    @Test
    void getCurrentUserTest(){
        String email = "test@example.com";
        Users mockUser = new Users();
        mockUser.setEmail(email);

        UserResponse mockDto = new UserResponse(1L, "First", "Last",
                email, "123", UserRoles.CUSTOMER, OffsetDateTime.now());

        when(userRepo.findByEmail(any())).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(any())).thenReturn(mockDto);

        UserResponse user = userService.getCurrentUser(email);

        assertNotNull(user);
        assertEquals("test@example.com",user.email());
        assertEquals("First",user.firstName());

        verify(userRepo).findByEmail("test@example.com");
        verify(userMapper).toDto(mockUser);
    }

    @Test
    void getCurrentUser_NotFoundTest(){
        String email = "test@example.com";

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> {
            userService.getCurrentUser(email);
        });

        verify(userMapper,never()).toDto(any());
    }


    @Test
    void getAllUsersTest(){
        Users user1 = new Users();
        user1.setEmail("user1@test.com");
        Users user2 = new Users();
        user2.setEmail("user2@test.com");

        List<Users> mockUsers = List.of(user1, user2);

        UserResponse dto1 = new UserResponse(1L, "U1", "L1", "user1@test.com",
                "123", UserRoles.CUSTOMER, OffsetDateTime.now());
        UserResponse dto2 = new UserResponse(2L, "U2", "L2", "user2@test.com",
                "456", UserRoles.CUSTOMER, OffsetDateTime.now());

        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);
        when(userRepo.findAll()).thenReturn(mockUsers);

        List<UserResponse> list = userService.getAllUsers();

        assertNotNull(list);
        assertEquals("user1@test.com",list.get(0).email());
        assertEquals("U1",list.get(0).firstName());

        verify(userRepo).findAll();
        verify(userMapper,times(2)).toDto(any());
    }

    @Test
    void verifyUserTest(){
        LoginDTO login = new LoginDTO("test@example.com","rawPassword");
        String mockToken = "eyJhbGci...exampleToken";

        Authentication mockAuth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(jwtUtilService.generateToken(login.email())).thenReturn(mockToken);

        String resultToken = userService.verifyUser(login);

        assertEquals(mockToken, resultToken);


        verify(authenticationManager).authenticate(argThat(auth ->
                auth.getPrincipal().equals(login.email()) && auth.getCredentials().equals(login.password())));
        verify(jwtUtilService).generateToken(login.email());

        assertEquals(mockAuth, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void verifyUserWrongCredentialsTest(){
        LoginDTO login = new LoginDTO("wrong@example.com","rawPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad Credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            userService.verifyUser(login);
        });
    }


    @Test
    void deleteCurrentUserTest(){
        String email = "test@example.com";
        Long userId = 46L;
        Users mockUser = new Users();
        mockUser.setEmail(email);
        mockUser.setId(userId);

        when(userRepo.findByEmail(any())).thenReturn(Optional.of(mockUser));

        userService.deleteCurrentUser(email);

        verify(userRepo).findByEmail(email);
        verify(addressRepository).deleteByUser_Id(userId);
        verify(cartRepository).deleteByUser_Id(userId);

        verify(userRepo).deleteById(userId);

    }

    @Test
    void deleteCurrentUser_ShouldThrowException_WhenUserNotFound() {
        // 1. Arrange
        String email = "ghost@test.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        // 2. Act & Assert
        assertThrows(UserNotFound.class, () -> {
            userService.deleteCurrentUser(email);
        });

        // 3. Verify NO deletions happened
        // The never() matcher ensures these lines were completely skipped
        verify(addressRepository, never()).deleteByUser_Id(any());
        verify(cartRepository, never()).deleteByUser_Id(any());
        verify(userRepo, never()).deleteById(any());
    }
}
