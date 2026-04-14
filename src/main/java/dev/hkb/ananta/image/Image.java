package dev.hkb.ananta.image;

import dev.hkb.ananta.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String imageName;

    @Column(name = "type")
    private String imageType;

    @Column(name = "img_url")
    private String imageUrl;

    @Column(name = "cloudinary_id")
    private String cloudinaryPublicId;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Image() {
    }

    public Image(String imageName, String imageType, String imageUrl, String cloudinaryPublicId) {
        this.imageName = imageName;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.cloudinaryPublicId = cloudinaryPublicId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCloudinaryPublicId() {
        return cloudinaryPublicId;
    }

    public void setCloudinaryPublicId(String cloudinaryPublicId) {
        this.cloudinaryPublicId = cloudinaryPublicId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
