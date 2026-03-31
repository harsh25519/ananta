package dev.hkb.ananta.order;

import dev.hkb.ananta.order.dto.CreateOrderRequest;
import dev.hkb.ananta.order.dto.OrderResponse;
import dev.hkb.ananta.security.utils.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@AuthenticationPrincipal UserPrincipal principal){
        List<OrderResponse> orders = orderService.getOrders(principal.getUsername());

        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<?> computeOrder(@AuthenticationPrincipal UserPrincipal principal,
                                          @Valid @RequestBody CreateOrderRequest request
                                          ){
        OrderResponse response = orderService.computeOrder(principal.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@AuthenticationPrincipal UserPrincipal principal,
                                         @PathVariable Long orderId){
        OrderResponse response = orderService.cancelOrder(principal.getUsername(), orderId);
        return ResponseEntity.ok(response);
    }
}
