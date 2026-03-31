package dev.hkb.ananta.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payments, Long> {

    Optional<Payments> findByGatewayReferenceId(String plinkId);
}
