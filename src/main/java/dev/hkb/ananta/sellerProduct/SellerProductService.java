package dev.hkb.ananta.sellerProduct;

import dev.hkb.ananta.order.Orders;
import dev.hkb.ananta.sellerProduct.dto.SellerProductBaseResponse;
import dev.hkb.ananta.sellerProduct.dto.SellerProductFullResponse;
import dev.hkb.ananta.sellerProduct.dto.UpdateSellerProductRequest;

import java.util.List;
import java.util.Set;

public interface SellerProductService {

    List<SellerProductBaseResponse> browseProducts(String productName, Long category, Set<Long> tags, String sortBy, boolean direction);

    List<SellerProductBaseResponse> getProductList(String email);

    SellerProductBaseResponse updateProduct(Long sellerProductId, UpdateSellerProductRequest request, String email);

    SellerProductFullResponse showSellerProduct(Long sellerProductId, String email);

    void deleteProduct(Long sellerProductId, String email);

    void decreaseInventory(Orders order);
}
