package dev.hkb.ananta.requestDTO;

import dev.hkb.ananta.constants.UserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(@NotBlank(message = "Enter First Name") String firstName,
                                @NotBlank(message = "Enter Last Name") String lastName,
                                @Email @NotBlank String email,
                                @NotBlank String password,
                                @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid phone number") String phoneNumber,
                                @NotNull UserRoles roles
                          ) {
}
