package dev.hkb.ananta.dto.sellerProduct;

import dev.hkb.ananta.constants.ProductStatus;

import java.math.BigDecimal;

public record SellerProductFullResponse(
        Long productId,
        String productName,
        Long sellerId,
        String sellerName,
        String productDescription,
        double averageRating,
        Long ratings,
        BigDecimal price,
        int quantity,
        ProductStatus status
) {
}
