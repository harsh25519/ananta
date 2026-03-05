package dev.hkb.ananta.dao;

import dev.hkb.ananta.entity.Seller;
import dev.hkb.ananta.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsByUser(Users ur);

    Object findByUser(Users user);
}
