package dev.hkb.ananta.requestDTO;

import dev.hkb.ananta.constants.ProductStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record CreateProductRequest(@NotBlank String name,

                                   @NotBlank String description,

                                   @NotNull
                                   @PositiveOrZero
                                   @DecimalMin("0.00")
                                   @Digits(integer = 8, fraction = 2)
                                   BigDecimal price,

                                   @NotNull
                                   @Positive
                                   Long categoryId,

                                   @NotNull
                                   @Positive
                                   Long manufacturerId,

                                   @NotNull ProductStatus status,

                                   @NotEmpty Set<Long> tagIds
                                   ) {
}
