package dev.hkb.ananta.payment;

import dev.hkb.ananta.payment.dto.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "paymentId", source = "id")
    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toPaymentDto(Payments payments);
}
