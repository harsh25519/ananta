package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
