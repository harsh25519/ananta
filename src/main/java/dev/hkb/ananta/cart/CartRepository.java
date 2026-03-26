package dev.hkb.ananta.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("Select c FROM Cart c join fetch c.cartItems ci join c.user u where u.email = :email ")
    Optional<Cart> findByUserEmailWithCartItems(@Param("email") String email);

    @Query("select c from Cart c join c.user u where u.email = :email")
    Optional<Cart> findByUserEmail(@Param("email") String username);

}
