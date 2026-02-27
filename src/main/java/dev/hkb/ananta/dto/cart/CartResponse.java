package dev.hkb.ananta.dto.cart;

import java.util.List;

public record CartResponse(
        Long cartId,
        Long userId,
        List<CartItemResponse> item
) {
}
