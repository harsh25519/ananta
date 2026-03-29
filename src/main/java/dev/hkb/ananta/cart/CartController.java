package dev.hkb.ananta.cart;

import dev.hkb.ananta.cart.dto.CartResponse;
import dev.hkb.ananta.cart.dto.CreateCartItemRequest;
import dev.hkb.ananta.security.utils.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /// Get my cart (CUSTOMER ONLY)
    @GetMapping
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserPrincipal principal){
        CartResponse cart = cartService.getCart(principal.getUsername());
        return ResponseEntity.ok(cart);
    }

    /// add an item to my cart
    @PostMapping("/item")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody CreateCartItemRequest request,
                                           @AuthenticationPrincipal UserPrincipal principal){
        cartService.addItemToCart(request, principal.getUsername());
        return ResponseEntity.ok(Map.of("Message:" , "Item Added to Cart"));
    }

    /// cart item id
    /// Update cart item by quantity (CUSTOMER ONLY)
    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartItemId,
                                            @RequestParam Integer quantity,
                                            @AuthenticationPrincipal UserPrincipal principal){
        CartResponse cart = cartService.updateCartItem(cartItemId, quantity, principal.getUsername());
        return ResponseEntity.ok(cart);
    }

    /// Removal of cart item from cart by Customer
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId,
                                            @AuthenticationPrincipal UserPrincipal principal){
        CartResponse cart = cartService.deleteItem(cartItemId, principal.getUsername());
        return ResponseEntity.ok(cart);
    }

    /// Empty cart
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserPrincipal principal){

        CartResponse cart = cartService.clearCart(principal.getUsername());
        return ResponseEntity.ok(cart);
    }

}
