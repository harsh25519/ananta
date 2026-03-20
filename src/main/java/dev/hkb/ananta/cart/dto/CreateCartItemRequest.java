package dev.hkb.ananta.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequest(
//        cartId it will be provided by authenticated user in service layer
        @NotNull Long productId,

        @Positive Integer quantity
) {
}
