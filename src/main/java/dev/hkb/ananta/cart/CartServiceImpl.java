package dev.hkb.ananta.cart;

import dev.hkb.ananta.cart.dto.CartResponse;
import dev.hkb.ananta.cart.dto.CreateCartItemRequest;
import dev.hkb.ananta.sellerProduct.SellerProduct;
import dev.hkb.ananta.sellerProduct.SellerProductRepository;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{


    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final SellerProductRepository sellerProductRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper, SellerProductRepository sellerProductRepository, CartItemRepository cartItemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.sellerProductRepository = sellerProductRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public CartResponse getCart(String email) {

        Cart cart = cartRepository.findByUserEmailWithCartItems(email)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    Users user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    newCart.setUser(user);
                    cartRepository.save(newCart);
                    return newCart;
                });

        return cartMapper.toCartDto(cart);
    }

    @Transactional
    @Override
    public void addItemToCart(CreateCartItemRequest request, String username) {

        Cart cart = cartRepository.findByUserEmail(username)
                .orElseGet(() ->{
                    Users user = userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        SellerProduct sellerProduct = sellerProductRepository.findById(request.productId())
                .orElseThrow(() -> new RuntimeException("Seller Product not found"));

        if (sellerProduct.getQuantity() < request.quantity()) {
            throw new RuntimeException("Only " + sellerProduct.getQuantity() + " units available");
        }

        Optional<CartItem> existing = cartItemRepository.findByCartAndSellerProduct(cart, sellerProduct);

        CartItem cartItem;
        if(existing.isPresent()){
            cartItem = existing.get();
            if (sellerProduct.getQuantity() < cartItem.getQuantity() + request.quantity()) {
                throw new RuntimeException("Only " + sellerProduct.getQuantity() + " units available");
            }
            cartItem.setQuantity(cartItem.getQuantity() + request.quantity());
        }else{
            cartItem = cartMapper.toCartItemEntity(request);
            cartItem.setCart(cart);
            cartItem.setSellerProduct(sellerProduct);
            cart.getCartItems().add(cartItem);
            cartItem.setPriceAtTime(sellerProduct.getPrice());
        }
        cartItemRepository.save(cartItem);
    }

    @Transactional
    @Override
    public CartResponse updateCartItem(Long cartItemId, Integer quantity, String username) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart Item not found"));

        if(!cartItem.getCart().getUser().getEmail().equals(username)){
            throw new RuntimeException("User not authorized");
        }

        if(quantity > cartItem.getSellerProduct().getQuantity()){
            throw new RuntimeException("Insufficient stock for this update");
        }
        if(quantity == 0){
            return deleteItem(cartItemId, username);
        }
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return cartMapper.toCartDto(cartItem.getCart());
    }

    @Transactional
    @Override
    public CartResponse deleteItem(Long cartItemId, String username) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart Item not found"));

        Cart cart = cartItem.getCart();

        if(!cartItem.getCart().getUser().getEmail().equals(username)){
            throw new RuntimeException("User not authorized");
        }

        cartItemRepository.delete(cartItem);
        cart.getCartItems().remove(cartItem);

        return cartMapper.toCartDto(cart);
    }

    @Transactional
    @Override
    public CartResponse clearCart(String email) {
        Cart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Cart does not exist"));

        // Because of orphanRemoval, this triggers a DELETE for every
        // single CartItem that was in this list.
        // we can also do it manually by traversing cartItemsRepo and deleting every item with cart id
        cart.getCartItems().clear();

        Cart savedCart = cartRepository.saveAndFlush(cart);

        return cartMapper.toCartDto(savedCart);
    }
}
