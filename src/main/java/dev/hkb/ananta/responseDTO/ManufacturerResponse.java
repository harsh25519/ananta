package dev.hkb.ananta.responseDTO;

import dev.hkb.ananta.constants.StatusEnum;

public record ManufacturerResponse(
        Long id,
        String brandName,
        String licenseKey,
        StatusEnum status
) {
}
