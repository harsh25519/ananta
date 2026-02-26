package dev.hkb.ananta.requestDTO;

import dev.hkb.ananta.constants.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateManufacturerRequest(
        @NotBlank String brandName,
        @NotBlank String licenseKey,
        @NotNull StatusEnum status
) {
}
