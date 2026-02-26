package dev.hkb.ananta.entity;

import dev.hkb.ananta.constants.StatusEnum;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "manufacturer")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "brand_name", nullable = false, unique = true, length = 100)
    private String brandName;

    @Column(name = "license_key", nullable = false, unique = true, length = 100)
    private String licenseKey;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @OneToMany(mappedBy = "manufacturer")
    private List<Product> products;

    // Constructors
    public Manufacturer() {
    }

    public Manufacturer(String brandName, String licenseKey, StatusEnum status) {
        this.brandName = brandName;
        this.licenseKey = licenseKey;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
