package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
