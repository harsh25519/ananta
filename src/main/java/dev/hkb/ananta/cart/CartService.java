package dev.hkb.ananta.cart;

import dev.hkb.ananta.cart.dto.CartResponse;
import dev.hkb.ananta.cart.dto.CreateCartItemRequest;

public interface CartService {

    CartResponse getCart(String email);

    void addItemToCart(CreateCartItemRequest request, String username);

    CartResponse updateCartItem(Long cartItemId, Integer quantity, String username);

    CartResponse deleteItem(Long cartItemId, String username);

    CartResponse clearCart(String email);
}
