package dev.hkb.ananta.seller;

import dev.hkb.ananta.security.utils.UserPrincipal;
import dev.hkb.ananta.seller.dto.CreateSellerRequest;
import dev.hkb.ananta.seller.dto.SellerResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    private SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /// Already existing customer can apply for seller
    @PostMapping("/applications")
    public ResponseEntity<?> applyForSeller(@Valid @RequestBody CreateSellerRequest csr,
                                            @AuthenticationPrincipal UserPrincipal principal){
        SellerResponse sr = sellerService.applyForSeller(csr, principal.getUsername());
        return ResponseEntity.ok(sr);
    }

    /// Get profile for Seller
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/me")
    public ResponseEntity<?> getSellerProfile(@AuthenticationPrincipal UserPrincipal principal){
        SellerResponse sr = sellerService.findByEmail(principal.getUsername());
        return ResponseEntity.ok(sr);
    }

}
