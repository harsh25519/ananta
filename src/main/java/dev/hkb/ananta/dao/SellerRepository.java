package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
