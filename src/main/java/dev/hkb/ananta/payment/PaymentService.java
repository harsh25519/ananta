package dev.hkb.ananta.payment;

import com.razorpay.RazorpayException;
import dev.hkb.ananta.cart.CartService;
import dev.hkb.ananta.constants.OrderStatus;
import dev.hkb.ananta.constants.PaymentStatus;
import dev.hkb.ananta.exceptionHandler.InsufficientStock;
import dev.hkb.ananta.exceptionHandler.OrderNotFound;
import dev.hkb.ananta.exceptionHandler.PaymentNotFound;
import dev.hkb.ananta.order.OrderItem;
import dev.hkb.ananta.order.OrderRepository;
import dev.hkb.ananta.order.Orders;
import dev.hkb.ananta.payment.dto.PaymentResponse;
import dev.hkb.ananta.sellerProduct.SellerProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private final OrderRepository orderRepository;
    private final RazorpayService razorpayService;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;
    private final PaymentMapper paymentMapper;
    private final SellerProductService sellerProductService;

    @Autowired
    public PaymentService(OrderRepository orderRepository, RazorpayService razorpayService, PaymentRepository paymentRepository, CartService cartService, PaymentMapper paymentMapper, SellerProductService sellerProductService, SellerProductService sellerProductService1) {
        this.orderRepository = orderRepository;
        this.razorpayService = razorpayService;
        this.paymentRepository = paymentRepository;
        this.cartService = cartService;
        this.paymentMapper = paymentMapper;
        this.sellerProductService = sellerProductService1;
    }

    public String processPayment(String username, Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFound("Order not found."));

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
            for(OrderItem oi : order.getOrderItemList()){
                if(oi.getSellerProduct().getQuantity() < oi.getQuantity()){
                    throw new InsufficientStock("Stock is less than required.");
                }
            }
            return razorpayService.createPaymentLink(order);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public PaymentResponse checkCallback(String status, String paymentId, String plinkId) {

        Payments paymentRecord = paymentRepository.findByGatewayReferenceId(plinkId)
                .orElseThrow(() -> new PaymentNotFound("Payment record not found for link: " + plinkId));

        if ("paid".equals(status)) {
            // 2. Update Payment Record
            paymentRecord.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentRecord.setGatewayTransactionId(paymentId);

            // 3. Update the associated Order
            Orders order = paymentRecord.getOrder();
            order.setOrderStatus(OrderStatus.PAID);
            sellerProductService.decreaseInventory(order);

            // 4. (Optional) Clear Cart here or via an Event
            cartService.clearCart(order.getUser().getEmail());
        } else {
            paymentRecord.setPaymentStatus(PaymentStatus.FAILED);
        }

        return paymentMapper.toPaymentDto(paymentRecord);
    }
}
