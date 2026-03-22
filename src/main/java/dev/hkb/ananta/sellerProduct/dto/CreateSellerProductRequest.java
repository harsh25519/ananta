package dev.hkb.ananta.sellerProduct.dto;

import dev.hkb.ananta.constants.ProductStatus;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateSellerProductRequest(
        @NotNull Long productId,

        @NotNull
        @PositiveOrZero
        @Digits(integer = 8, fraction = 2)
        BigDecimal price,

        @Positive(message = "Enter a valid quantity") Integer quantity,

        @NotNull ProductStatus status
) {
}
