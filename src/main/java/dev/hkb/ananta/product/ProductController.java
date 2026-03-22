package dev.hkb.ananta.product;

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
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper, ProductRepository productRepository) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequest cpr){
        ProductResponse product = productService.createProduct(cpr);
        return ResponseEntity.ok(product);
    }

    // update it later with parameter for searching and sorting
    // get product to sell by seller
    @GetMapping
    public ResponseEntity<?> getProductsToSell(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Set<Long> tags
            ){
        return ResponseEntity.ok(productService.getProducts(category, tags));
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyToSell(@Valid @RequestBody CreateSellerProductRequest request,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        productService.sellerApplyToProduct(request, userPrincipal.getUsername());
        return ResponseEntity.ok(Map.of(
                "message", "Application submitted successfully",
                "status", "PENDING"
        ));
    }

    @GetMapping("/queue")
    public ResponseEntity<?> getPendingQueue(){
        return ResponseEntity.ok(productService.getPendingApprovals());
    }

    @PostMapping("/approve/{listingId}")
    public ResponseEntity<?> resolveRequest(@PathVariable Long listingId,
                                            @RequestParam(defaultValue = "true") boolean status){
        String message = productService.approveListing(listingId, status);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
