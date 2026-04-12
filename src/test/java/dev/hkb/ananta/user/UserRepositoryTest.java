package dev.hkb.ananta.user;

import dev.hkb.ananta.constants.UserRoles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_ShouldReturnUser_WhenExists(){
        String email = "nissan@gtr.com";

        Users mockUser = new Users("skyline", "stututu", email,
                "hello","9683842345", UserRoles.CUSTOMER);
        userRepository.save(mockUser);

        Users ur = userRepository.findByEmail(email).get();

        assertThat(ur).isNotNull();
        assertThat(ur.getEmail()).isEqualTo(email);
        assertThat(ur.getFirstName()).isEqualTo("skyline");
    }
}
