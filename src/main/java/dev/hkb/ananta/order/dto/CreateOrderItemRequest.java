package dev.hkb.ananta.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/// not needed still I created as we will directly fetch from cart
public record CreateOrderItemRequest(
        @NotNull Long productId,

        @Positive Integer quantity
) {
}
