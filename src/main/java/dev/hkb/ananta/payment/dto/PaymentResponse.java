package dev.hkb.ananta.dto.payment;

import dev.hkb.ananta.constants.CurrencyEnum;
import dev.hkb.ananta.constants.PaymentMethod;
import dev.hkb.ananta.constants.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
        Long paymentId,
        Long orderId,
        PaymentMethod paymentMethod,
        String gatewayTransactionId,
        BigDecimal amount,
        CurrencyEnum currency,
        PaymentStatus paymentStatus,
        OffsetDateTime createdAt
) {
}
