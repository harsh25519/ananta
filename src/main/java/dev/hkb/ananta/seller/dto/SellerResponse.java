package dev.hkb.ananta.dto.seller;

import dev.hkb.ananta.constants.CountryEnum;
import dev.hkb.ananta.constants.StateEnum;
import dev.hkb.ananta.constants.UserRoles;

import java.time.OffsetDateTime;

public record SellerResponse(Long id,
                             String userName,
                             String email,
                             String shopName,
                             UserRoles role,
                             String license,
                             String addressLine,
                             String city,
                             StateEnum state,
                             String pinCode,
                             CountryEnum country,
                             OffsetDateTime userCreatedAt
) {
}
