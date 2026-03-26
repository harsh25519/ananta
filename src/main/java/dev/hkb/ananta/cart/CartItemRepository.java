package dev.hkb.ananta.cart;

import dev.hkb.ananta.sellerProduct.SellerProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndSellerProduct(Cart cart, SellerProduct sellerProduct);
}
