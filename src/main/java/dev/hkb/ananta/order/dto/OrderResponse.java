package dev.hkb.ananta.dto.order;

import dev.hkb.ananta.constants.OrderStatus;
import dev.hkb.ananta.address.dto.AddressResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        Long userId,
        String userName,
        AddressResponse billingAddress,
        AddressResponse shippingAddress,
        List<OrderItemResponse> orderItemList,
        BigDecimal totalPrice,
        OffsetDateTime createdAt,
        OrderStatus orderStatus
) {
}
