package dev.hkb.ananta.controller;

import dev.hkb.ananta.dto.seller.CreateSellerRequest;
import dev.hkb.ananta.dto.seller.SellerResponse;
import dev.hkb.ananta.service.SellerService;
import dev.hkb.ananta.utils.UserPrincipal;
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

    @PostMapping("/apply")
    public ResponseEntity<?> applyForSeller(@Valid @RequestBody CreateSellerRequest csr,
                                            @AuthenticationPrincipal UserPrincipal principal){
        SellerResponse sr = sellerService.applyForSeller(csr, principal.getUsername());
        return ResponseEntity.ok(sr);
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/profile")
    public ResponseEntity<?> getSellerProfile(@AuthenticationPrincipal UserPrincipal principal){
        SellerResponse sr = sellerService.findByEmail(principal.getUsername());
        return ResponseEntity.ok(sr);
    }

}
