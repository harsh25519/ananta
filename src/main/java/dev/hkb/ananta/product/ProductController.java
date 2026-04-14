package dev.hkb.ananta.product;

import dev.hkb.ananta.image.CloudinaryService;
import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.sellerProduct.dto.CreateSellerProductRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CloudinaryService imageService;

    @Autowired
    public ProductController(ProductService productService, CloudinaryService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    /// Create Product by ADMIN
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequest cpr){
        ProductResponse product = productService.createProduct(cpr);
        return ResponseEntity.ok(product);
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
}
