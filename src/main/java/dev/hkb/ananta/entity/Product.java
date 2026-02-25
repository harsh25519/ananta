package dev.hkb.ananta.entity;

import dev.hkb.ananta.constants.ProductStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @Column(name = "status")
    private ProductStatus status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_tag",
            joinColumns = @JoinColumn(name ="product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tags> tagSet = new HashSet<>();

    //Constructors
    public Product() {
    }

    public Product(String name, String description, Category category, Manufacturer manufacturer, ProductStatus status) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.manufacturer = manufacturer;
        this.status = status;
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Tags> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<Tags> tagSet) {
        this.tagSet = tagSet;
    }
}
