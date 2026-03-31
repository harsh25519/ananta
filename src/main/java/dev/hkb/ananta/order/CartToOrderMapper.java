package dev.hkb.ananta.order;

import dev.hkb.ananta.address.Address;
import dev.hkb.ananta.cart.Cart;
import dev.hkb.ananta.cart.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CartToOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "sellerProduct", source = "cartItem.sellerProduct")
    @Mapping(target = "purchasePrice", source = "cartItem.priceAtTime")
    @Mapping(target = "quantity", source = "cartItem.quantity")
    OrderItem cartItemToOrderItem(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "cart.user")
    @Mapping(target = "totalPrice", expression = "java(getTotalPrice(cart))")
    @Mapping(target = "orderItemList", source = "cart.cartItems")
    @Mapping(target = "billingAddress", source = "billing")
    @Mapping(target = "shippingAddress", source = "shipping")
    Orders cartToOrder(Cart cart, Address billing, Address shipping);

    @AfterMapping
    default void linkOrderItems(@MappingTarget Orders order){
        if(order != null && order.getOrderItemList() != null) {
            order.getOrderItemList().forEach(item -> {
                        item.setOrder(order);
//                        order.getOrderItemList().add(item);  this will create infinite loop problem
                    }
            );
        }
    }

    default BigDecimal getTotalPrice(Cart cart){
        BigDecimal price = BigDecimal.ZERO;
        for(CartItem ci : cart.getCartItems()){
            var p = ci.getSellerProduct().getPrice();
            price = price.add(p.multiply(BigDecimal.valueOf(ci.getQuantity())));
            ci.setPriceAtTime(p);
        }
        return price;
    }

}
