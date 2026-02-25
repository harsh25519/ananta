package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
