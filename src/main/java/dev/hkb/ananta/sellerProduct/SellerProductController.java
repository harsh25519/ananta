package dev.hkb.ananta.sellerProduct;

import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.sellerProduct.dto.SellerProductBaseResponse;
import dev.hkb.ananta.sellerProduct.dto.SellerProductFullResponse;
import dev.hkb.ananta.sellerProduct.dto.UpdateSellerProductRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/seller-products")
public class SellerProductController {

    private final SellerProductService sellerProductService;

    @Autowired
    public SellerProductController(SellerProductService sellerProductService) {
        this.sellerProductService = sellerProductService;
    }

    /// Show all products at the front page
    @GetMapping("/browse")
    public ResponseEntity<?> getProducts(@RequestParam(required = false) String productName,
                                         @RequestParam(required = false) Long category,
                                         @RequestParam(required = false) Set<Long> tags,
                                         @RequestParam(required = false, defaultValue = "price") String sortBy,
                                         @RequestParam(required = false, defaultValue = "true") boolean direction){
        List<SellerProductBaseResponse> list = sellerProductService.browseProducts(productName, category, tags, sortBy, direction);
        return ResponseEntity.ok(list);
    }

    /// mapping for products shown by seller
    @GetMapping("/me")
    public ResponseEntity<?> getMyProducts(@AuthenticationPrincipal UserPrincipal principal){
        List<SellerProductBaseResponse> list = sellerProductService.getProductList(principal.getUsername());

        return ResponseEntity.ok(list);
    }

    /// Seller can update price and status of product anytime
    @PutMapping("/{sellerProductId}")
    public ResponseEntity<?> updatePriceAndStatus(@PathVariable Long sellerProductId,
                                                  @Valid @RequestBody UpdateSellerProductRequest request,
                                                  @AuthenticationPrincipal UserPrincipal userPrincipal){
        SellerProductBaseResponse response = sellerProductService.updateProduct(sellerProductId, request, userPrincipal.getUsername());
        return ResponseEntity.ok(response);
    }

    /// See full description of product listed (Anyone authenticated)
    @GetMapping("/{sellerProductId}")
    public ResponseEntity<?> showSellerProduct(@PathVariable Long sellerProductId,
                                               @AuthenticationPrincipal UserPrincipal principal){
        SellerProductFullResponse response = sellerProductService.showSellerProduct(sellerProductId, principal.getUsername());
        return ResponseEntity.ok(response);
    }


    /// cannot hard delete we can soft delete as otherwise it will create problem for old orders
    @DeleteMapping("/{sellerProductId}")
    public ResponseEntity<?> deleteSellerProduct(@PathVariable Long sellerProductId,
                                                  @AuthenticationPrincipal UserPrincipal principal){
        sellerProductService.deleteProduct(sellerProductId, principal.getUsername());
        return ResponseEntity.ok(Map.of("Message:" , "Seller Product Successfully Deleted"));
    }

}
