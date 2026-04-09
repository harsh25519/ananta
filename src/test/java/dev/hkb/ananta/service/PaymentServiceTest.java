package dev.hkb.ananta.service;

import dev.hkb.ananta.order.OrderRepository;
import dev.hkb.ananta.order.Orders;
import dev.hkb.ananta.payment.PaymentService;
import dev.hkb.ananta.payment.RazorpayService;
import dev.hkb.ananta.user.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private RazorpayService razorpayService;

    @Test
    void processPaymentTest(){
        String username = "test@example.com";
        Long orderId = 4L;
        Users mockUser = new Users();
        mockUser.setEmail(username);

        Orders mockOrder = new Orders();
        mockOrder.setId(orderId);
        mockOrder.setUser(mockUser);

        when(orderRepository.findById(any())).thenReturn(Optional.of(mockOrder));
    }
}
