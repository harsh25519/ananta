package dev.hkb.ananta.service;

import dev.hkb.ananta.dto.seller.CreateSellerRequest;
import dev.hkb.ananta.dto.seller.SellerResponse;

public interface SellerService {

    SellerResponse applyForSeller(CreateSellerRequest sellerDto, String email);

    SellerResponse findByEmail(String username);
}
