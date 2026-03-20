package dev.hkb.ananta.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "seller_product_id"})
)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_product_id", nullable = false)
    private SellerProduct sellerProduct;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price_at_time", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtTime;

    //Constructors
    public CartItem() {
    }

    public CartItem(Cart cart, SellerProduct sellerProduct, int quantity, BigDecimal priceAtTime) {
        this.cart = cart;
        this.sellerProduct = sellerProduct;
        this.quantity = quantity;
        this.priceAtTime = priceAtTime;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public SellerProduct getSellerProduct() {
        return sellerProduct;
    }

    public void setSellerProduct(SellerProduct sellerProduct) {
        this.sellerProduct = sellerProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtTime() {
        return priceAtTime;
    }

    public void setPriceAtTime(BigDecimal priceAtTime) {
        this.priceAtTime = priceAtTime;
    }
}
