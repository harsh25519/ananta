package dev.hkb.ananta.order.dto;

import dev.hkb.ananta.address.dto.AddressResponse;
import dev.hkb.ananta.constants.OrderStatus;

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
