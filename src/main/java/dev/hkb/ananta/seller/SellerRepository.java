package dev.hkb.ananta.seller;

import dev.hkb.ananta.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsByUser(Users ur);

    Object findByUser(Users user);
}
