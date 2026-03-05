package dev.hkb.ananta.dto.address;

import dev.hkb.ananta.constants.CountryEnum;
import dev.hkb.ananta.constants.StateEnum;

// keeping it simple will update it later
public record AddressResponse(
        Long addressId,
        String username,
        String phoneNumber,
        String addressLine,
        String city,
        StateEnum state,
        String pinCode,
        CountryEnum country
) {
}
