package dev.hkb.ananta.order;

import dev.hkb.ananta.address.Address;
import dev.hkb.ananta.address.AddressRepository;
import dev.hkb.ananta.cart.Cart;
import dev.hkb.ananta.cart.CartItem;
import dev.hkb.ananta.cart.CartRepository;
import dev.hkb.ananta.constants.OrderStatus;
import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.exceptionHandler.*;
import dev.hkb.ananta.order.dto.CreateOrderRequest;
import dev.hkb.ananta.order.dto.OrderResponse;
import dev.hkb.ananta.user.UserRepository;
import dev.hkb.ananta.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {


    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AddressRepository addressRepository;
    private final CartToOrderMapper cartToOrderMapper;
    private final CartRepository cartRepository;

    @Autowired
    public OrderService(UserRepository userRepository, OrderRepository orderRepository, OrderMapper orderMapper, AddressRepository addressRepository, CartToOrderMapper cartToOrderMapper, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.addressRepository = addressRepository;
        this.cartToOrderMapper = cartToOrderMapper;
        this.cartRepository = cartRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(String username) {
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFound("User Not found"));

        /// Move to security layer
        if(!user.getRole().equals(UserRoles.CUSTOMER)){
            throw new RuntimeException("You are not customer");
        }

        List<Orders> list = orderRepository.findAllByUser_Id(user.getId());

        return list.stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Transactional
    public OrderResponse computeOrder(String username, CreateOrderRequest request) {
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFound("User Not found"));
        Cart cart = cartRepository.findByUserEmail(username)
                .orElseThrow(() -> new CartNotFound("Cart not found"));

        for(CartItem ci : cart.getCartItems()){
            if(ci.getSellerProduct().getQuantity() <= ci.getQuantity()){
                throw new InsufficientStock("Insufficient stock for product: " + ci.getSellerProduct().getProduct().getName()
                        + ". Available: " + ci.getSellerProduct().getQuantity());
            }
        }

        Address billingAddress = addressRepository.findById(request.billingAddressId())
                .orElseThrow(() -> new AddressNotFound("Address could not be found"));
        Address shippingAddress = addressRepository.findById(request.shippingAddressId())
                .orElseThrow(() -> new AddressNotFound("Address could not be found"));

        Orders order = cartToOrderMapper.cartToOrder(cart, billingAddress, shippingAddress);
        order.setOrderStatus(OrderStatus.PENDING);

        order.setUser(user);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Transactional
    public OrderResponse cancelOrder(String username, Long orderId){
        Users user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFound("User Not found"));

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound("Order can't be found"));
        if (order.getOrderStatus() == OrderStatus.SHIPPED || order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Order cannot be cancelled once it has been shipped.");
        }

        if(order.getOrderStatus() == OrderStatus.CANCELED){
            throw new RuntimeException("Order is already cancelled.");
        }

        /// Will remove it in near future as it will create UX bad
        if(order.getOrderStatus() == OrderStatus.PAID){
            throw new RuntimeException("Order cannot be cancelled after it is paid");
        }

        if(!order.getUser().getEmail().equals(username)){
            throw new UserNotAuthorized("This user is not authorized.");
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }




}
