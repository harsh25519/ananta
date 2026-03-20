package dev.hkb.ananta.mapper;

import dev.hkb.ananta.dto.manufacturer.CreateManufacturerRequest;
import dev.hkb.ananta.dto.manufacturer.ManufacturerResponse;
import dev.hkb.ananta.entity.Manufacturer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

    Manufacturer toEntity(CreateManufacturerRequest cmr);

    ManufacturerResponse toDto(Manufacturer man);
}
