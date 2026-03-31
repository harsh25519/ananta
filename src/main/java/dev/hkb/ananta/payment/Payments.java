package dev.hkb.ananta.payment;

import dev.hkb.ananta.constants.CurrencyEnum;
import dev.hkb.ananta.constants.PaymentMethod;
import dev.hkb.ananta.constants.PaymentStatus;
import dev.hkb.ananta.order.Orders;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //Create it many to one and unique false as payment can fail multiple times
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @Column(name = "amount",precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private CurrencyEnum currency = CurrencyEnum.INR;

    /// suppose it for now
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_methods", nullable = false)
    private PaymentMethod paymentMethod = PaymentMethod.DEBIT_CARD;

    /// for plink id
    @Column(name = "gateway_ref_id", unique = true)
    private String gatewayReferenceId;

    @Column(name = "transaction_id", unique = true, nullable = true)
    private String gatewayTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    //Constructors
    public Payments() {
    }

    public Payments(Orders order, BigDecimal amount, PaymentMethod paymentMethod,  String gatewayReferenceId, String gatewayTransactionId, PaymentStatus paymentStatus, OffsetDateTime createdAt) {
        this.order = order;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.gatewayReferenceId = gatewayReferenceId;
        this.gatewayTransactionId = gatewayTransactionId;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders orders) {
        this.order = orders;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethods) {
        this.paymentMethod = paymentMethods;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String transactionId) {
        this.gatewayTransactionId = transactionId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public String getGatewayReferenceId() {
        return gatewayReferenceId;
    }

    public void setGatewayReferenceId(String gatewayReferenceId) {
        this.gatewayReferenceId = gatewayReferenceId;
    }
}
