package dev.hkb.ananta.user;

import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.security.utils.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getUserProfileTest_ShouldReturnLoggedInUserProfile() throws Exception {

        Users u1 = new Users("Haggu", "Malgudi","malgudi@gmail.com",
                "hashedPass","9581265475", UserRoles.CUSTOMER);
        u1 = userRepository.save(u1);

        UserPrincipal fakePrincipal = new UserPrincipal(u1);

        mockMvc.perform(get("/users/me")
                .with(user(fakePrincipal))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Haggu"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void showAllUsersTest() throws Exception {
        Users u1 = new Users("Haggu", "Malgudi","malgudi@gmail.com",
                "hashedPass","9581265475", UserRoles.CUSTOMER);
        u1 = userRepository.save(u1);
        Users u2 = new Users("Shiva", "Malgudi","shiva@gmail.com",
                "hash","9542265475", UserRoles.CUSTOMER);
        u2 = userRepository.save(u2);

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Haggu"));
    }
}
