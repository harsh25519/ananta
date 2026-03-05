package dev.hkb.ananta.service;

import dev.hkb.ananta.dto.manufacturer.CreateManufacturerRequest;
import dev.hkb.ananta.dto.manufacturer.ManufacturerResponse;
import jakarta.validation.Valid;

public interface ManufacturerService {
    ManufacturerResponse addManufacturer(@Valid CreateManufacturerRequest cmr);
}
