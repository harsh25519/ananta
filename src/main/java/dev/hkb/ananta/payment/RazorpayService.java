package dev.hkb.ananta.payment;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import dev.hkb.ananta.constants.PaymentStatus;
import dev.hkb.ananta.order.Orders;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RazorpayService {

    private final PaymentRepository paymentRepository;
    private final OrderToPaymentMapper orderToPaymentMapper;
    private final RazorpayClient razorpayClient; // Mark as final
    @Value("${WEB_NAME}")
    private String webName;

    // Inject the keys directly into the constructor
    @Autowired
    public RazorpayService(
            PaymentRepository paymentRepository,
            OrderToPaymentMapper orderToPaymentMapper,
            @Value("${RAZORPAY_API_KEY}") String razorpayKey,
            @Value("${RAZORPAY_API_SECRET}") String razorpaySecret) throws RazorpayException {

        this.paymentRepository = paymentRepository;
        this.orderToPaymentMapper = orderToPaymentMapper;

        // Now the keys are guaranteed to be present
        this.razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);
    }

    public String createPaymentLink(Orders order) throws RazorpayException {

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", order.getTotalPrice().multiply(new BigDecimal(100)).longValue());
        paymentLinkRequest.put("currency", order.getCurrency());
        String uniqueRef = "order_id_" + order.getId() +  "_" + System.currentTimeMillis();
        paymentLinkRequest.put("reference_id", uniqueRef);

        JSONObject customer = new JSONObject();
        customer.put("name", (order.getUser().getFirstName() + " " + order.getUser().getLastName()));
        customer.put("email", order.getUser().getEmail());

        JSONObject notify = new JSONObject();
        notify.put("email", "true");

        paymentLinkRequest.put("callback_url", webName + "/ananta/v1/payments/callback");
        paymentLinkRequest.put("callback_method", "get");

        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

        Payments dbPayments = orderToPaymentMapper.orderToPayment(order);
        dbPayments.setGatewayReferenceId(uniqueRef);
        dbPayments.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.save(dbPayments);

        return payment.get("short_url").toString();
    }
}
