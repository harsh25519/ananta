package dev.hkb.ananta.address;

import dev.hkb.ananta.address.dto.AddressResponse;
import dev.hkb.ananta.address.dto.CreateAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(CreateAddressRequest car);

    @Mapping(source = "id", target = "addressId")
    @Mapping(target = "username", expression = "java(address.getUser().getFirstName() + \" \" + address.getUser().getLastName())")
    AddressResponse toDto(Address address);

}
