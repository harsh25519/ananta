package dev.hkb.ananta.dto.cart;

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
