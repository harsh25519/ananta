package dev.hkb.ananta.dto.seller;

import dev.hkb.ananta.constants.CountryEnum;
import dev.hkb.ananta.constants.StateEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateSellerRequest(@NotBlank String shopName,

                                  @NotBlank
                                  @Size(min = 12, max = 15)
                                  String license,

                                  @NotBlank String addressLine,

                                  @NotBlank String city,

                                  @NotNull StateEnum state,

                                  @NotBlank
                                  @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid Pin code")
                                  String pinCode,

                                  @NotNull CountryEnum country
                                  ) {
}
