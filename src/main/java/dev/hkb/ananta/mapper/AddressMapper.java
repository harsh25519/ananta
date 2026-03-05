package dev.hkb.ananta.mapper;

import dev.hkb.ananta.dto.address.AddressResponse;
import dev.hkb.ananta.dto.address.CreateAddressRequest;
import dev.hkb.ananta.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(CreateAddressRequest car);

    @Mapping(source = "id", target = "addressId")
    @Mapping(target = "username", expression = "java(address.getUser().getFirstName() + \" \" + address.getUser().getLastName())")
    AddressResponse toDto(Address address);

}
