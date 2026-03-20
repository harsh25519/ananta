package dev.hkb.ananta.cart.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long cartItemId,
        Long cartId,
        Long productId,
        String productName,
        int quantity,
        BigDecimal price
) {
}
