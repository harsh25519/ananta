package dev.hkb.ananta.payment;

import dev.hkb.ananta.payment.dto.PaymentResponse;
import dev.hkb.ananta.security.utils.UserPrincipal;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}/create")
    public ResponseEntity<?> processPayment(@AuthenticationPrincipal UserPrincipal principal,
                                            @PathVariable Long orderId
                                            ){
        String url = paymentService.processPayment(principal.getUsername(), orderId);
        return ResponseEntity.ok(Map.of("Payment Link: ", url));
    }

    @GetMapping("/callback")
    @SecurityRequirements()
    public ResponseEntity<?> paymentCallback(
            @RequestParam String razorpay_payment_link_status,
            @RequestParam String razorpay_payment_id,
            @RequestParam String razorpay_payment_link_reference_id
                                             ){
        PaymentResponse response = paymentService.checkCallback(razorpay_payment_link_status,razorpay_payment_id,razorpay_payment_link_reference_id);
        return ResponseEntity.ok(response);
    }


}
