package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
