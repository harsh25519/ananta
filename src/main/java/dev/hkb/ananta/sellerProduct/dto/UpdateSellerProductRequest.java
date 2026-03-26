package dev.hkb.ananta.sellerProduct.dto;

import dev.hkb.ananta.constants.ProductStatus;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateSellerProductRequest(
        @Positive
        BigDecimal price,

        ProductStatus productStatus// ACTIVE OR HIDDEN
        ) {
}
