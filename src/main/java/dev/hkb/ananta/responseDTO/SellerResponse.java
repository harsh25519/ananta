package dev.hkb.ananta.responseDTO;

import dev.hkb.ananta.constants.CountryEnum;
import dev.hkb.ananta.constants.StateEnum;

import java.time.OffsetDateTime;

public record SellerResponse(Long id,
                             String userName,
                             String email,
                             String shopName,
                             String license,
                             String addressLine,
                             String city,
                             StateEnum state,
                             String pinCode,
                             CountryEnum country,
                             OffsetDateTime userCreatedAt
) {
}
