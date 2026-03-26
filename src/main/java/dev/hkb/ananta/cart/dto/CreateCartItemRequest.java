package dev.hkb.ananta.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequest(
//        cartId it will be provided by authenticated user in service layer

        //it is a seller productId
        @NotNull Long productId,

        @Positive Integer quantity
) {
    public CreateCartItemRequest {
        if (quantity == null) quantity = 1;
        // No "this.productId = productId" needed!
    }
}
