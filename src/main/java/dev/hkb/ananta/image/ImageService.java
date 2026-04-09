package dev.hkb.ananta.image;

import dev.hkb.ananta.exceptionHandler.ImageNotFound;
import dev.hkb.ananta.exceptionHandler.ProductNotFound;
import dev.hkb.ananta.product.Product;
import dev.hkb.ananta.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {


    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ImageService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void addImage(Long id, MultipartFile imageFile){
        try {
            Image image = new Image();

            Product product = productRepository.findById(id)
                            .orElseThrow(() -> new ProductNotFound("Product does not exist"));
            image.setImageName(imageFile.getOriginalFilename());
            image.setImageType(imageFile.getContentType());
            image.setImage(imageFile.getBytes());
            image.setProduct(product);

            imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException(e);
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

        if (product != null) {
            product.setProductImage(null);
        }
        img.setProduct(null);
        imageRepository.delete(img);
    }
}
