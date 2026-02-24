package dev.hkb.ananta.entity;

import dev.hkb.ananta.constants.ProductStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "seller_product",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id","seller_id"})
)
public class SellerProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus productStatus;

    //Constructor
    public SellerProduct() {
    }

    public SellerProduct(Product product, Seller seller, BigDecimal price, int quantity, ProductStatus productStatus) {
        this.product = product;
        this.seller = seller;
        this.price = price;
        this.quantity = quantity;
        this.productStatus = productStatus;
    }

    //Getters And Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
