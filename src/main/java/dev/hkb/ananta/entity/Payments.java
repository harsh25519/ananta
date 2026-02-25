package dev.hkb.ananta.entity;

import dev.hkb.ananta.constants.PaymentMethod;
import dev.hkb.ananta.constants.PaymentStatus;
import jakarta.persistence.*;

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
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethods;

    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    //Constructors
    public Payments() {
    }

    public Payments(Orders orders, PaymentMethod paymentMethods, String transactionId, PaymentStatus paymentStatus, OffsetDateTime createdAt) {
        this.orders = orders;
        this.paymentMethods = paymentMethods;
        this.transactionId = transactionId;
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

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public PaymentMethod getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(PaymentMethod paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
}
