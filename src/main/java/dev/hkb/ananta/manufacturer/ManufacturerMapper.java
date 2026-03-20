package dev.hkb.ananta.manufacturer;


import dev.hkb.ananta.manufacturer.dto.CreateManufacturerRequest;
import dev.hkb.ananta.manufacturer.dto.ManufacturerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

    Manufacturer toEntity(CreateManufacturerRequest cmr);

    ManufacturerResponse toDto(Manufacturer man);
}
