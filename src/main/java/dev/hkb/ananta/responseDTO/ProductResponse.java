package dev.hkb.ananta.responseDTO;

import dev.hkb.ananta.constants.ProductStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public record ProductResponse(Long Id,
                              String name,
                              String description,
                              BigDecimal price,
                              String categoryName,
                              String manufacturerName,
                              ProductStatus status,
                              Set<String> tagNames,
                              OffsetDateTime createdAt
                              ) {
}
