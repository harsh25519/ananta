package dev.hkb.ananta.image;

import dev.hkb.ananta.product.Product;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.BinaryJdbcType;

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

    @Lob
    @JdbcType(BinaryJdbcType.class)
    @Column(name = "img")
    private byte[] image;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Image() {
    }

    public Image(String imageName, String imageType, byte[] image) {
        this.imageName = imageName;
        this.imageType = imageType;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
