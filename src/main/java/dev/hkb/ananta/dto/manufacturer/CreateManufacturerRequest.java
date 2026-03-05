package dev.hkb.ananta.dto.manufacturer;

import dev.hkb.ananta.constants.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateManufacturerRequest(
        @NotBlank String brandName,

        @NotBlank
        @Size(min = 12, max = 15)
        String licenseKey,

        @NotNull StatusEnum status
) {
}
