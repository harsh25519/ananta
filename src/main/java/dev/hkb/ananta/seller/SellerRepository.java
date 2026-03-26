package dev.hkb.ananta.seller;

import dev.hkb.ananta.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsByUser(Users ur);

    Optional<Seller> findByUser(Users user);
}
