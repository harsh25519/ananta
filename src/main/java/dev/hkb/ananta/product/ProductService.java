package dev.hkb.ananta.product;

import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import dev.hkb.ananta.sellerProduct.dto.CreateSellerProductRequest;
import dev.hkb.ananta.sellerProduct.dto.SellerProductBaseResponse;

import java.util.List;
import java.util.Set;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest cpr);

    List<ProductResponse> getProducts(Long category, Set<Long> tag);

    void sellerApplyToProduct(CreateSellerProductRequest cspr, String sellerId);

    List<SellerProductBaseResponse> getPendingApprovals();

    String approveListing(Long sellerProductId, boolean approved);

}
