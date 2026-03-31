package dev.hkb.ananta.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /// Upload image of product
    /// id === productId
    @PostMapping("/{id}/images")
    public ResponseEntity<?> uploadImages(@PathVariable Long id,
                                          @RequestPart("file") MultipartFile imageFile
    ){
        imageService.addImage(id, imageFile);
        return ResponseEntity.ok(Map.of("Message: ", "Image successfully added"));
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId){
        Image productImage = imageService.getImage(productId);
        if (productImage == null || productImage.getImage() == null) {
            // Returning 404 is cleaner for the frontend/browser
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(productImage.getImageType()))
                .header(HttpHeaders.CACHE_CONTROL, "max-age=3600") // store for 1 hour locally
                .body(productImage.getImage());
    }

    @DeleteMapping("/{productId}/images")
    public ResponseEntity<?> deleteImage(@PathVariable Long productId){
        imageService.removeImage(productId);
        return ResponseEntity.ok(Map.of("Message: ", "Image deleted successfully"));
    }
}
