package dev.hkb.ananta.product;

import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(CreateProductRequest productRequest);

    ProductResponse toDto(Product product);
}
