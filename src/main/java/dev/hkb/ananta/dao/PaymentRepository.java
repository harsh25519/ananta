package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payments, Long> {
}
