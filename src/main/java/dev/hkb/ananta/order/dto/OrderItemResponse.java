package dev.hkb.ananta.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long orderId,
        Long productId,// seller product id
        String imageUrl,
        String productName,
        int quantity,
        BigDecimal purchasePrice,
        BigDecimal totalPrice
) {
}
