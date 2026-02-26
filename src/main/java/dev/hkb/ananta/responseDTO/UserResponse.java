package dev.hkb.ananta.responseDTO;

import dev.hkb.ananta.constants.UserRoles;

import java.time.OffsetDateTime;

public record UserResponse(Long id,
                           String firstName,
                           String lastName,
                           String email,
                           String phoneNumber,
                           UserRoles role,
                           OffsetDateTime createdAt
) {
}
