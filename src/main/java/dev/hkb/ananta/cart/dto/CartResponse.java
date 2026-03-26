package dev.hkb.ananta.cart.dto;

import java.util.List;

public record CartResponse(
        Long cartId,
        Long userId,
        List<CartItemResponse> cartItems
) {
}
