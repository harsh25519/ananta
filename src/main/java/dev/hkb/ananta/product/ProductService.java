package dev.hkb.ananta.product;

import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest cpr);
}
