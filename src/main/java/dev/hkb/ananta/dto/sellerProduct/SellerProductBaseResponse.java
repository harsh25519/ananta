package dev.hkb.ananta.dto.sellerProduct;

import dev.hkb.ananta.constants.ProductStatus;

import java.math.BigDecimal;

public record SellerProductBaseResponse(
        Long productId,
        String productName,
        Long sellerId,
        String sellerName,
        BigDecimal price,
        int quantity,
        ProductStatus status
) {
}
