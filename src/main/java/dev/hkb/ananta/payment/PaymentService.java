package dev.hkb.ananta.payment;

import com.razorpay.RazorpayException;
import dev.hkb.ananta.cart.CartService;
import dev.hkb.ananta.constants.OrderStatus;
import dev.hkb.ananta.constants.PaymentStatus;
import dev.hkb.ananta.order.OrderRepository;
import dev.hkb.ananta.order.Orders;
import dev.hkb.ananta.payment.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final OrderRepository orderRepository;
    private final RazorpayService razorpayService;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentService(OrderRepository orderRepository, RazorpayService razorpayService, PaymentRepository paymentRepository, CartService cartService, PaymentMapper paymentMapper) {
        this.orderRepository = orderRepository;
        this.razorpayService = razorpayService;
        this.paymentRepository = paymentRepository;
        this.cartService = cartService;
        this.paymentMapper = paymentMapper;
    }

    public String processPayment(String username, Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found."));

        if (order.getOrderStatus() == OrderStatus.PAID) {
            throw new RuntimeException("This order is already paid for!");
        }
        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new RuntimeException("Cannot pay for a canceled order.");
        }

        if(!order.getUser().getEmail().equals(username)){
            throw new RuntimeException("User not authorized");
        }
        try {
            return razorpayService.createPaymentLink(order);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }

    }

    public PaymentResponse checkCallback(String status, String paymentId, String plinkId) {

        Payments paymentRecord = paymentRepository.findByGatewayReferenceId(plinkId)
                .orElseThrow(() -> new RuntimeException("Payment record not found for link: " + plinkId));

        if ("paid".equals(status)) {
            // 2. Update Payment Record
            paymentRecord.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRecord.setGatewayTransactionId(paymentId);

            // 3. Update the associated Order
            Orders order = paymentRecord.getOrder();
            order.setOrderStatus(OrderStatus.PAID);

            // 4. (Optional) Clear Cart here or via an Event
            cartService.clearCart(order.getUser().getEmail());
        } else {
            paymentRecord.setPaymentStatus(PaymentStatus.FAILED);
        }

        return paymentMapper.toPaymentDto(paymentRecord);
    }
}
