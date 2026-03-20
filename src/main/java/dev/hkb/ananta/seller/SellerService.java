package dev.hkb.ananta.seller;

import dev.hkb.ananta.seller.dto.CreateSellerRequest;
import dev.hkb.ananta.seller.dto.SellerResponse;

public interface SellerService {


    SellerResponse applyForSeller(CreateSellerRequest sellerDto, String email);

    SellerResponse findByEmail(String username);
}
