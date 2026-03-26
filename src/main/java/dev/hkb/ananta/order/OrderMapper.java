package dev.hkb.ananta.order;

import dev.hkb.ananta.address.AddressMapper;
import dev.hkb.ananta.order.dto.CreateOrderRequest;
import dev.hkb.ananta.order.dto.OrderItemResponse;
import dev.hkb.ananta.order.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class}
)
public interface OrderMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "orderItem.sellerProduct.id", target = "productId")
    @Mapping(source = "orderItem.sellerProduct.product.name", target = "productName")
    @Mapping(target = "totalPrice", expression = "java(orderItem.getTotalPrice())")
    OrderItemResponse toOrderItemDto(OrderItem orderItem);

    Orders toOrderEntity(CreateOrderRequest request);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "userName", expression = "java(order.getUser().getFirstName() + \"\" + order.getUser().getLastName())")
    OrderResponse toOrderDto(Orders order);

    default List<OrderItemResponse> mapOrderItemToOrderItemResponse(List<OrderItem> orderItemList){
        if(orderItemList == null)return null;

        return orderItemList.stream()
                .map(this::toOrderItemDto)
                .toList();
    }

}
