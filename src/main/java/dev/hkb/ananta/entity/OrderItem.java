package dev.hkb.ananta.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "seller_product_id"})
)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_product_id")
    private SellerProduct sellerProduct;

    @Column(name = "purchase_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "quantity")
    private int quantity;

    //Constructors
    public OrderItem() {
    }

    public OrderItem(Orders order, SellerProduct sellerProduct, BigDecimal purchasePrice, int quantity) {
        this.order = order;
        this.sellerProduct = sellerProduct;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
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

    public void setOrder(Orders order) {
        this.order = order;
    }

    public SellerProduct getSellerProduct() {
        return sellerProduct;
    }

    public void setSellerProduct(SellerProduct sellerProduct) {
        this.sellerProduct = sellerProduct;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
