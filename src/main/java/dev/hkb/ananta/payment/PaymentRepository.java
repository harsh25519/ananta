package dev.hkb.ananta.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payments, Long> {

    Optional<Payments> findByGatewayReferenceId(String plinkId);

    @Modifying
    @Query("UPDATE Payments p SET p.paymentStatus = 'CANCELLED' WHERE p.paymentStatus = 'PENDING' AND p.createdAt < :cutoffTime")
    int cancelAllPendingPayments(@Param("cutoffTime") OffsetDateTime cutoffTime);
}
