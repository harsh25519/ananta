package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.SellerProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerProductRepository extends JpaRepository<SellerProduct, Long> {
}
