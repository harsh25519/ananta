package dev.hkb.ananta.user;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.exceptionHandler.UserNotFound;
import dev.hkb.ananta.security.jwt.JwtUtilService;
import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;
    @MockitoBean
    private JwtUtilService jwtUtilService;

    @Test
    void showProfile_ShouldReturn200Test() throws Exception {
        String email = "haggu@gmail.com";
        UserResponse ur = new UserResponse(7L, "haggu","niggu", email,
                "9574612385", UserRoles.CUSTOMER, OffsetDateTime.now());

        UserPrincipal fakePrincipal = mock(UserPrincipal.class);
        when(fakePrincipal.getUsername()).thenReturn(email);

        when(userService.getCurrentUser(email)).thenReturn(ur);

        mockMvc.perform(get("/users/me")
                        .with(user(fakePrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        verify(userService).getCurrentUser(email);
    }

    @Test
    void showProfile_UserNotFoundTest() throws Exception {
        String email = "haggu@gmail.com";

        UserPrincipal fakePrincipal = mock(UserPrincipal.class);
        when(fakePrincipal.getUsername()).thenReturn(email);

        when(userService.getCurrentUser(email)).thenThrow(UserNotFound.class);

        mockMvc.perform(get("/users/me")
                        .with(user(fakePrincipal))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).getCurrentUser(email);
    }

    @Test
    void showUsersTest() throws Exception {
        String email = "haggu@gmail.com";

        UserPrincipal fakePrincipal = mock(UserPrincipal.class);
        when(fakePrincipal.getUsername()).thenReturn(email);

        UserResponse ur1 = new UserResponse(7L, "haggu","niggu", "niggu@gmail.com",
                "9574612385", UserRoles.CUSTOMER, OffsetDateTime.now());
        UserResponse ur2 = new UserResponse(9L, "chaggu","maggie", "maggie@gmail.com",
                "9574612385", UserRoles.CUSTOMER, OffsetDateTime.now());

        List<UserResponse> list = List.of(ur1, ur2);

        when(userService.getAllUsers()).thenReturn(list);

        mockMvc.perform(get("/users")
                .with(user(fakePrincipal))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("niggu@gmail.com"))
                .andExpect(jsonPath("$[1].email").value("maggie@gmail.com"));

        verify(userService).getAllUsers();
    }


}
