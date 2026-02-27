package dev.hkb.ananta.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long orderId,
        Long productId,
        String productName,
        int quantity,
        BigDecimal purchasePrice
) {
}
