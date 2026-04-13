package dev.hkb.ananta.payment;

import dev.hkb.ananta.cart.CartService;
import dev.hkb.ananta.constants.CurrencyEnum;
import dev.hkb.ananta.constants.PaymentMethod;
import dev.hkb.ananta.constants.PaymentStatus;
import dev.hkb.ananta.constants.UserRoles;
import dev.hkb.ananta.payment.dto.PaymentResponse;
import dev.hkb.ananta.security.jwt.JwtUtilService;
import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.sellerProduct.SellerProductService;
import dev.hkb.ananta.user.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtilService jwtUtilService;
    @MockitoBean
    private PaymentRepository paymentRepository;
    @MockitoBean
    private SellerProductService sellerProductService;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private PaymentService paymentService;

    @Test
    void shouldReturnPaymentLink() throws Exception {
        Users fakeUser = new Users();
        fakeUser.setEmail("test@example.com");
        fakeUser.setRole(UserRoles.CUSTOMER);
        UserPrincipal principal = new UserPrincipal(fakeUser);

        String mockUrl = "https://razorpay.me/fake_link";
        when(paymentService.processPayment(anyString(), anyLong())).thenReturn(mockUrl);

        mockMvc.perform(post("/payments/1/create")
                        .with(csrf())
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Payment Link: ']").value(mockUrl));
    }


    @Test
    void shouldHandleCallback_WhenRazorpayCalls() throws Exception {
        Users fakeUser = new Users();
        fakeUser.setEmail("test@example.com");
        fakeUser.setRole(UserRoles.CUSTOMER);
        UserPrincipal principal = new UserPrincipal(fakeUser);
        PaymentResponse response = new PaymentResponse(
                987L, 45L, PaymentMethod.DEBIT_CARD, "ref_001",
                "tans_345",BigDecimal.TEN, CurrencyEnum.INR, PaymentStatus.SUCCESS,
                OffsetDateTime.now());

        when(paymentService.checkCallback(anyString(), anyString(), anyString()))
                .thenReturn(response);

        mockMvc.perform(get("/payments/callback")
                        .param("razorpay_payment_link_status", "paid")
                        .param("razorpay_payment_id", "pay_987")
                        .param("razorpay_payment_link_reference_id", "ref_001")
                        .with(user(principal)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentStatus").value("SUCCESS"));
    }
}
