package dev.hkb.ananta.sellerProduct.dto;

import dev.hkb.ananta.constants.ProductStatus;

import java.math.BigDecimal;

public record SellerProductBaseResponse(
        Long productId,
        String productName,
        Long sellerProductId,
        Long sellerId,
        String sellerName,
        BigDecimal price,
        int quantity,
        ProductStatus status
) {
}
