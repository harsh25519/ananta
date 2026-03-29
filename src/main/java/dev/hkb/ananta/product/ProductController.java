package dev.hkb.ananta.product;

import dev.hkb.ananta.image.Image;
import dev.hkb.ananta.image.ImageService;
import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.sellerProduct.dto.CreateSellerProductRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;

    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper, ImageService imageService, ProductRepository productRepository) {
        this.productService = productService;
        this.imageService = imageService;
    }

    /// Create Product by ADMIN
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequest cpr){
        ProductResponse product = productService.createProduct(cpr);
        return ResponseEntity.ok(product);
    }

    /// Upload image of product
    /// id === productId
    @PostMapping("/{id}/images")
    public ResponseEntity<?> uploadImages(@PathVariable Long id,
                                          @RequestParam("file") MultipartFile imageFile
                                          ){
        imageService.addImage(id, imageFile);
        return ResponseEntity.ok(Map.of("Message: ", "Image successfully added"));
    }

    /// update it later with parameter for searching and sorting
    /// get product to sell by seller
    /// Here Seller can see products which he/she can list to sell
    @GetMapping
    public ResponseEntity<?> getProductsToSell(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Set<Long> tags
            ){
        return ResponseEntity.ok(productService.getProducts(category, tags));
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId){
        Image productImage = imageService.getImage(productId);
        if(productImage == null){
            return null;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(productImage.getImageType())) // e.g., "image/jpeg"
                .body(productImage.getImage());
    }


    /// Seller can apply to sell a particular item from the above list
    @PostMapping("/applications")
    public ResponseEntity<?> applyToSell(@Valid @RequestBody CreateSellerProductRequest request,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        productService.sellerApplyToProduct(request, userPrincipal.getUsername());
        return ResponseEntity.ok(Map.of(
                "message", "Application submitted successfully",
                "status", "PENDING"
        ));
    }

    /// Admin can get pending request queued
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingQueue(){
        return ResponseEntity.ok(productService.getPendingApprovals());
    }

    /// Admin can approve any pending request
    @PostMapping("/approve/{listingId}")
    public ResponseEntity<?> resolveRequest(@PathVariable Long listingId,
                                            @RequestParam(defaultValue = "true") boolean status){
        String message = productService.approveListing(listingId, status);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @DeleteMapping("/{productId}/images")
    public ResponseEntity<?> deleteImage(@PathVariable Long productId){
        imageService.removeImage(productId);
        return ResponseEntity.ok(Map.of("Message: ", "Image deleted successfully"));
    }
}
