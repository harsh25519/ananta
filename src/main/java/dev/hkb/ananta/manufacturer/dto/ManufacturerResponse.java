package dev.hkb.ananta.manufacturer.dto;

import dev.hkb.ananta.constants.StatusEnum;

public record ManufacturerResponse(
        Long id,
        String brandName,
        String licenseKey,
        StatusEnum status
) {
}
