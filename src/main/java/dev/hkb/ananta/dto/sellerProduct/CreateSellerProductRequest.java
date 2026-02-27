package dev.hkb.ananta.dto.sellerProduct;

import dev.hkb.ananta.constants.ProductStatus;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateSellerProductRequest(
        @NotNull Long productId,

        @NotNull Long sellerId,

        @NotNull
        @PositiveOrZero
        @Digits(integer = 8, fraction = 2)
        BigDecimal price,

        @Positive(message = "Enter a valid quantity") Integer quantity,

        @NotNull ProductStatus status
) {
}
