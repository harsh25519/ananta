package dev.hkb.ananta.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.user.email = :email")
    Optional<Cart> findByUserEmailWithCartItems(@Param("email") String email);

    @Query("select c from Cart c join c.user u where u.email = :email")
    Optional<Cart> findByUserEmail(@Param("email") String username);

    @Modifying
    @Query("delete from Cart c where c.user.id = :userId")
    void deleteByUser_Id(@Param("userId") Long userId);

}
