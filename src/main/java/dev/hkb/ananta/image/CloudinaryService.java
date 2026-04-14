package dev.hkb.ananta.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dev.hkb.ananta.exceptionHandler.ImageNotFound;
import dev.hkb.ananta.exceptionHandler.ProductNotFound;
import dev.hkb.ananta.product.Product;
import dev.hkb.ananta.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(ProductRepository productRepository,
                        ImageRepository imageRepository,
                        @Value("${cloudinary.cloud-name}") String cloudName,
                        @Value("${cloudinary.api-key}") String apiKey,
                        @Value("${cloudinary.api-secret}") String apiSecret) {

        this.productRepository = productRepository;
        this.imageRepository = imageRepository;

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }


    @Transactional
    public void addImage(Long id, MultipartFile imageFile) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFound("Product does not exist"));

            Map uploadFile = cloudinary.uploader().upload(imageFile.getBytes(), Map.of());

            String imageUrl = uploadFile.get("url").toString();
            String publicId = uploadFile.get("public_id").toString();

            Image image = new Image();
            image.setImageName(imageFile.getOriginalFilename());
            image.setImageType(imageFile.getContentType());
            image.setImageUrl(imageUrl);
            image.setCloudinaryPublicId(publicId);
            image.setProduct(product);

            imageRepository.save(image);
        }  catch (IOException e) {
            throw new RuntimeException("Failed to upload image to cloudinary.");
        }
    }

    public Image getImage(Long productId){
        Image img = imageRepository.findByProduct_Id(productId)
                .orElse(null);

        return img;
    }

    @Transactional
    public void removeImage(Long productId){
        Image img = imageRepository.findByProduct_Id(productId)
                .orElseThrow(() -> new ImageNotFound("Image does not exist"));

        Product product = img.getProduct();

        try {
            if(img.getCloudinaryPublicId() != null){
                cloudinary.uploader().destroy(String.valueOf(img.getCloudinaryPublicId()), Map.of());
            }

            if (product != null) {
                product.setProductImage(null);
            }
            img.setProduct(null);
            imageRepository.delete(img);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete Image from cloudinary.");
        }
    }



}
