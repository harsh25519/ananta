package dev.hkb.ananta.product;

import dev.hkb.ananta.product.dto.CreateProductRequest;
import dev.hkb.ananta.product.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // resolve these ignored things at the business logic
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    @Mapping(target = "tagSet", ignore = true)
    Product toEntity(CreateProductRequest productRequest);

    @Mapping(target = "categoryName", expression = "java(product.getCategory().getName())")
    @Mapping(target = "manufacturerName", expression = "java(product.getManufacturer().getBrandName())")
    @Mapping(source = "tagSet", target = "tagNames")
    ProductResponse toDto(Product product);
}
