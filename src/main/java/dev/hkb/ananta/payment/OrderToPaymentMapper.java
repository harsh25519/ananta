package dev.hkb.ananta.payment;

import dev.hkb.ananta.order.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderToPaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "amount", source = "order.totalPrice")
    @Mapping(target = "gatewayTransactionId", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    Payments orderToPayment(Orders order);
}
