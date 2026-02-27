package dev.hkb.ananta.dto.address;

import dev.hkb.ananta.constants.CountryEnum;
import dev.hkb.ananta.constants.StateEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAddressRequest(
//        Long userId, send automatically by business logic that's more secured
        @NotBlank
        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid phone number")
        String phoneNumber,

        @NotBlank
        @Size(max = 255)
        String addressLine,

        @NotBlank
        @Size(max = 50)
        String city,

        @NotNull StateEnum state,

        @NotBlank
        @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid Pin code")
        String pinCode,

        @NotNull CountryEnum country
) {
}
