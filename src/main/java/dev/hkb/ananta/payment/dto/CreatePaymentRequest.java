package dev.hkb.ananta.payment.dto;

import dev.hkb.ananta.constants.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull Long orderId,
        @NotNull PaymentMethod paymentMethod
) {
}
