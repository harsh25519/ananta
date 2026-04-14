package dev.hkb.ananta.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ImageController {

    private final CloudinaryService cloudinaryService;

    @Autowired
    public ImageController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    /// Upload image of product
    /// id === productId
    @PostMapping("/{id}/images")
    public ResponseEntity<?> uploadImages(@PathVariable Long id,
                                          @RequestPart("file") MultipartFile imageFile
    ){
        cloudinaryService.addImage(id, imageFile);
        return ResponseEntity.ok(Map.of("Message: ", "Image successfully added"));
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<?> getProductImage(@PathVariable Long productId){
        Image productImage = cloudinaryService.getImage(productId);
        if (productImage == null || productImage.getImageUrl() == null) {
            // Returning 404 is cleaner for the frontend/browser
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of("imageUrl", productImage.getImageUrl()));
    }

    @DeleteMapping("/{productId}/images")
    public ResponseEntity<?> deleteImage(@PathVariable Long productId){
        cloudinaryService.removeImage(productId);
        return ResponseEntity.ok(Map.of("Message: ", "Image deleted successfully"));
    }
}
