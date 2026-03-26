package dev.hkb.ananta.cart;

import dev.hkb.ananta.cart.dto.CartItemResponse;
import dev.hkb.ananta.cart.dto.CartResponse;
import dev.hkb.ananta.cart.dto.CreateCartItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "id", target = "cartId")
    @Mapping(source = "user.id", target = "userId")
    CartResponse toCartDto(Cart cart);


    CartItem toCartItemEntity(CreateCartItemRequest request);

    @Mapping(source = "id", target = "cartItemId")
    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "sellerProduct.id", target = "productId")
    @Mapping(source = "sellerProduct.product.name", target = "productName")
    @Mapping(source = "priceAtTime", target = "price")
    @Mapping(target = "itemTotal", ignore = true)
    CartItemResponse toCartItemDto(CartItem cartItem);


    default List<CartItemResponse> mapCartItemsToCartItemResponse(List<CartItem> cartItems){
        if(cartItems == null){
            return null;
        }

        return cartItems.stream()
                .map(this::toCartItemDto)
                .toList();
    }
}
