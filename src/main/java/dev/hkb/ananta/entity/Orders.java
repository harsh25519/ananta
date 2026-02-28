package dev.hkb.ananta.entity;

import dev.hkb.ananta.constants.CurrencyEnum;
import dev.hkb.ananta.constants.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id", nullable = false)
    private Address billingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;

    @Column(name = "total_price", nullable = false, precision = 12, scale =2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", precision = 12, scale = 2, nullable = false)
    private CurrencyEnum currency = CurrencyEnum.INR;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

//    status (CREATED, PAYMENT_PENDING, PAID, CANCELLED)
    //Create a field status to check the status of payments and make order and payment ( 1 - n)
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList;

    //Constructors
    public Orders() {
    }

    public Orders(Users user, Address billingAddress, Address shippingAddress,
                  BigDecimal totalPrice, OffsetDateTime createdAt) {
        this.user = user;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
