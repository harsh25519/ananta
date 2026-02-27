package dev.hkb.ananta.dto.order;

public record CreateOrderRequest(
//        Long userId we give it via auth in service
        //only address because the user can provide different address
        Long billingAddressId,
        Long shippingAddressId
) {
}
